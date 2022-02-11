/*
 * Copyright (c) 2022.
 *
 * @author Henrik Toivakka
 * @contact htoivakka@gmail.com
 */

package fi.hopeton.delegate;

import fi.hopeton.eventshuffle.api.event.ApiApiDelegate;
import fi.hopeton.eventshuffle.model.EventDto;
import fi.hopeton.eventshuffle.model.EventIdDto;
import fi.hopeton.eventshuffle.model.ProposedEventDto;
import fi.hopeton.jooq.Tables;
import fi.hopeton.jooq.tables.daos.EventsDao;
import fi.hopeton.jooq.tables.records.EventsRecord;
import fi.hopeton.jooq.tables.records.ProposedDatesRecord;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
public class EventDelegate implements ApiApiDelegate {

    private final DSLContext dslContext;

    public EventDelegate(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public ResponseEntity<EventIdDto> createEvent(ProposedEventDto proposedEventDto) {

        AtomicReference<Long> eventId = new AtomicReference<>(0L);

        dslContext.transaction(configuration -> {

            DSLContext transactionContext = DSL.using(configuration);

            EventsRecord eventsRecord = transactionContext.insertInto(Tables.EVENTS, Tables.EVENTS.NAME)
                    .values(proposedEventDto.getName())
                    .onConflictDoNothing()
                    .returning(Tables.EVENTS.EVENT_ID)
                    .fetchOne();

            List<ProposedDatesRecord> dates = proposedEventDto.getDates().stream().map(date -> {
                ProposedDatesRecord r = new ProposedDatesRecord();
                r.setEventId(Objects.requireNonNull(eventsRecord.getValue(Tables.EVENTS.EVENT_ID)).longValue());
                r.setEventDate(date);

                return r;
            }).collect(Collectors.toList());

            eventId.set(Objects.requireNonNull(eventsRecord.getValue(Tables.EVENTS.EVENT_ID)).longValue());

            transactionContext.batchInsert(dates).execute();
        });

        return ResponseEntity.ok(new EventIdDto().id(eventId.get()));
    }

    @Override
    public ResponseEntity<List<EventDto>> listAllEvents() {
        EventsDao dao = new EventsDao(dslContext.configuration());

        List<EventDto> eventDtos = dao.findAll().stream().map(event -> {
            EventDto eventDto = new EventDto();
            eventDto.setId(event.getEventId().longValue());
            eventDto.setName(event.getName());
            return eventDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(eventDtos);
    }
}
