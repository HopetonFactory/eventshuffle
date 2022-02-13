/*
 * Copyright (c) 2022.
 *
 * @author Henrik Toivakka
 * @contact htoivakka@gmail.com
 */

package fi.hopeton.delegate;

import fi.hopeton.eventshuffle.api.event.ApiApiDelegate;
import fi.hopeton.eventshuffle.model.*;
import fi.hopeton.helpers.CollectionUtils;
import fi.hopeton.helpers.StringUtils;
import fi.hopeton.jooq.Tables;
import fi.hopeton.jooq.tables.daos.EventsDao;
import fi.hopeton.jooq.tables.pojos.EventDate;
import fi.hopeton.jooq.tables.pojos.EventDateVote;
import fi.hopeton.jooq.tables.pojos.Events;
import fi.hopeton.jooq.tables.records.EventDateRecord;
import fi.hopeton.jooq.tables.records.EventDateVoteRecord;
import fi.hopeton.jooq.tables.records.EventsRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static fi.hopeton.jooq.Tables.*;

@Service
public class EventDelegate implements ApiApiDelegate {

    private final DSLContext dslContext;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public EventDelegate(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public ResponseEntity<EventResultResponseDto> eventResults(@NonNull Long id) {
        return null;
    }

    @Override
    public ResponseEntity<EventResponseDto> showEvent(@NonNull Long id) {

        LOGGER.debug("showEvent() with id={}", id);

        // Initialize DAO
        EventsDao dao = new EventsDao(dslContext.configuration());

        // Find event objects
        Events events = dao.findById(id);

        if (events == null) {
            return ResponseEntity.notFound().build();
        }

        List<SuitableDateDto> suitableDateDtos = getSuitableDates(id);

        // Initialize DTO
        EventResponseDto responseDto = new EventResponseDto();
        responseDto.setName(events.getName());
        responseDto.setId(events.getId());
        responseDto.setVotes(suitableDateDtos);

        return ResponseEntity.ok(responseDto);
    }

    private List<SuitableDateDto> getSuitableDates(Long eventId) {

        Map<EventDate, List<EventDateVote>> result = dslContext
                .select(EVENT_DATE.fields())
                .select(EVENT_DATE_VOTE.fields())
                .from(EVENT_DATE)
                .join(EVENT_DATE_VOTE).on(EVENT_DATE_VOTE.EVENT_DATE_ID.eq(EVENT_DATE.ID))
                .where(EVENT_DATE.EVENT_ID.equal(eventId))
                .fetchGroups(
                        r -> r.into(EVENT_DATE).into(EventDate.class),
                        r -> r.into(EVENT_DATE_VOTE).into(EventDateVote.class));

        // Map the result set into objects
        List<SuitableDateDto> suitableDateDtos = result.entrySet().stream().map(res -> {
            SuitableDateDto suitableDateDto = new SuitableDateDto();
            suitableDateDto.setDate(res.getKey().getProposedDate());
            suitableDateDto.setPeople(res.getValue().stream().map(EventDateVote::getVotingPerson).collect(Collectors.toList()));

            return suitableDateDto;
        }).collect(Collectors.toList());

        return suitableDateDtos;
    }

    @Override
    public ResponseEntity<EventVoteResponseDto> voteEvent(@NonNull Long id, @NonNull EventVoteDto eventVoteDto) {

        LOGGER.debug("voteEvent() with request={}", eventVoteDto.toString());

        if (StringUtils.isEmptyOrNull(eventVoteDto.getName()) || CollectionUtils.isEmptyOrNull(eventVoteDto.getVotes())) {
            return ResponseEntity.badRequest().build();
        }

        // Initialize atomic variables to catch variables from the lambda method
        AtomicLong eventId = new AtomicLong(0L);
        AtomicReference<String> eventName = new AtomicReference<>();

        // Start transaction for multiple queries
        dslContext.transaction(configuration -> {

            DSLContext transactionContext = DSL.using(configuration);

            EventsDao eventsDao = new EventsDao(transactionContext.configuration());

            // Find the event
            Events events = eventsDao.fetchOneById(id);
            eventId.set(events.getId());
            eventName.set(events.getName());

            // Craft where clause for clarity
            final Condition whereClause = EVENT_DATE.EVENT_ID.eq(events.getId())
                    .and(EVENT_DATE.PROPOSED_DATE.in(eventVoteDto.getVotes()));

            // Find the associated proposed days, which can be voted
            List<EventDateRecord> eventDateRecords = transactionContext
                    .selectFrom(Tables.EVENT_DATE)
                    .where(whereClause)
                    .fetch();

            List<EventDateVoteRecord> eventDateVoteRecords = new ArrayList<>();

            for (EventDateRecord eventDateRecord : eventDateRecords) {
                // Create record for batch insert
                EventDateVoteRecord eventDateVoteRecord = new EventDateVoteRecord();
                eventDateVoteRecord.setEventDateId(eventDateRecord.getId());
                eventDateVoteRecord.setVotingPerson(eventVoteDto.getName());

                eventDateVoteRecords.add(eventDateVoteRecord);
            }

            // Insert in a batch
            transactionContext
                    .batchInsert(eventDateVoteRecords).execute();
        });

        // Find the updated suitable dates
        List<SuitableDateDto> suitableDateDtos = getSuitableDates(id);

        // Create response DTO
        EventVoteResponseDto eventVoteResponseDto = new EventVoteResponseDto()
                .id(eventId.get())
                .name(eventName.get())
                .votes(suitableDateDtos);

        return ResponseEntity.ok(eventVoteResponseDto);
    }

    @Override
    public ResponseEntity<EventIdDto> createEvent(@NonNull ProposedEventDto proposedEventDto) {

        LOGGER.debug("createEvent() with request={}", proposedEventDto.toString());

        // Check for nulls
        if (StringUtils.isEmptyOrNull(proposedEventDto.getName()) || CollectionUtils.isEmptyOrNull(proposedEventDto.getDates())) {
            return ResponseEntity.badRequest().build();
        }

        // Initialize atomic variable to catch the ID from the lambda method
        AtomicReference<Long> eventId = new AtomicReference<>(0L);

        // Start a transaction for multiple database operations
        dslContext.transaction(configuration -> {

            DSLContext transactionContext = DSL.using(configuration);

            EventsRecord eventsRecord = transactionContext.insertInto(EVENTS, EVENTS.NAME)
                    .values(proposedEventDto.getName())
                    .onConflictDoNothing()
                    .returning(EVENTS.ID)
                    .fetchOne();

            List<EventDateRecord> dates = proposedEventDto.getDates().stream().map(date -> {
                EventDateRecord eventDateRecord = new EventDateRecord();
                eventDateRecord.setEventId(Objects.requireNonNull(eventsRecord.getValue(EVENTS.ID)));
                eventDateRecord.setProposedDate(date);

                return eventDateRecord;
            }).collect(Collectors.toList());

            eventId.set(Objects.requireNonNull(eventsRecord.getValue(EVENTS.ID)));

            // Insert in a batch
            transactionContext.batchInsert(dates).execute();
        });

        return ResponseEntity.ok(new EventIdDto().id(eventId.get()));
    }

    @Override
    public ResponseEntity<List<EventDto>> listAllEvents() {

        LOGGER.debug("listAllEvents()");

        // Initialize DAO
        EventsDao dao = new EventsDao(dslContext.configuration());

        // Find objects and map into DTO's
        List<EventDto> eventDtos = dao.findAll().stream().map(event -> {
            EventDto eventDto = new EventDto();
            eventDto.setId(event.getId());
            eventDto.setName(event.getName());
            return eventDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(eventDtos);
    }
}
