package fi.hopeton.repository;

import fi.hopeton.jooq.Tables;
import fi.hopeton.jooq.tables.daos.EventsDao;
import fi.hopeton.jooq.tables.pojos.EventDate;
import fi.hopeton.jooq.tables.pojos.EventDateVote;
import fi.hopeton.jooq.tables.pojos.Events;
import fi.hopeton.jooq.tables.records.EventDateRecord;
import fi.hopeton.jooq.tables.records.EventDateVoteRecord;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static fi.hopeton.jooq.Tables.*;
import static org.jooq.impl.DSL.countDistinct;
import static org.jooq.impl.DSL.select;

@Repository
public class EventRepository implements IEventRepository {

    private final DSLContext dslContext;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public EventRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    @Override
    public Map<EventDate, List<EventDateVote>> findVotedDates(Long eventId) {

        Map<EventDate, List<EventDateVote>> result = dslContext
                .select(EVENT_DATE.fields())
                .select(EVENT_DATE_VOTE.fields())
                .from(EVENT_DATE)
                .join(EVENT_DATE_VOTE).on(EVENT_DATE_VOTE.EVENT_DATE_ID.eq(EVENT_DATE.ID))
                .where(EVENT_DATE.EVENT_ID.equal(eventId))
                .fetchGroups(
                        r -> r.into(EVENT_DATE).into(EventDate.class),
                        r -> r.into(EVENT_DATE_VOTE).into(EventDateVote.class));

        return result;
    }

    @Override
    public Events findById(Long id) {
        // Initialize DAO
        EventsDao dao = new EventsDao(dslContext.configuration());

        Events events = dao.findById(id);

        return events;
    }

    @Override
    public void insertEventVotes(Events events, String votingPerson, List<LocalDate> votedDates) {
        // Start transaction for multiple queries
        dslContext.transaction(configuration -> {

            DSLContext transactionContext = DSL.using(configuration);

            // Craft where clause for clarity
            final Condition whereClause = EVENT_DATE.EVENT_ID.eq(events.getId())
                    .and(EVENT_DATE.PROPOSED_DATE.in(votedDates));

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
                eventDateVoteRecord.setVotingPerson(votingPerson);

                eventDateVoteRecords.add(eventDateVoteRecord);
            }

            // Insert in a batch
            transactionContext
                    .batchInsert(eventDateVoteRecords).execute();
        });
    }

    @Override
    public Long insertEvent(String eventName, List<LocalDate> proposedDates) {

        // Initialize atomic variable to catch the ID from the lambda method
        AtomicReference<Long> eventId = new AtomicReference<>(0L);

        // Start a transaction for multiple database operations
        dslContext.transaction(configuration -> {

            DSLContext transactionContext = DSL.using(configuration);

            Events events = transactionContext.insertInto(EVENTS, EVENTS.NAME)
                    .values(eventName)
                    .onConflictDoNothing()
                    .returning()
                    .fetchOne()
                    .into(Events.class);

            List<EventDateRecord> dates = proposedDates.stream().map(date -> {
                EventDateRecord eventDateRecord = new EventDateRecord();
                eventDateRecord.setEventId(Objects.requireNonNull(events.getId()));
                eventDateRecord.setProposedDate(date);

                return eventDateRecord;
            }).collect(Collectors.toList());

            eventId.set(Objects.requireNonNull(events.getId()));

            // Insert in a batch
            transactionContext.batchInsert(dates).execute();
        });

        return eventId.get();
    }

    @Override
    public List<Events> findAllEvents() {
        EventsDao dao = new EventsDao(dslContext.configuration());

        List<Events> events = dao.findAll();

        return events;
    }

    @Override
    public Map<EventDate, List<EventDateVote>> findSuitableDates(Long eventId) {

        // Compares the total amount of distinct voters between a specific date and total voters of the event
        final Condition whereClause = EVENT_DATE.EVENT_ID.eq(eventId).and(
                select(countDistinct(EVENT_DATE_VOTE.VOTING_PERSON))
                        .from(EVENT_DATE)
                        .join(EVENT_DATE_VOTE).on(EVENT_DATE_VOTE.EVENT_DATE_ID.eq(EVENT_DATE.ID))
                        .where(EVENT_DATE.EVENT_ID.eq(eventId))
                        .eq(select(countDistinct(EVENT_DATE_VOTE.VOTING_PERSON))
                                .from(EVENTS)
                                .join(EVENT_DATE).on(EVENT_DATE.EVENT_ID.eq(EVENTS.ID))
                                .join(EVENT_DATE_VOTE).on(EVENT_DATE_VOTE.EVENT_DATE_ID.eq(EVENT_DATE.ID))
                                .where(EVENT_DATE.EVENT_ID.eq(eventId))
                                .groupBy(EVENTS.NAME, EVENTS.ID)
                        ));

        // Execute query with the crafted where-clause, group the result by the proposed date
        List<EventDate> suitableDates = dslContext.select(EVENT_DATE.fields())
                .from(EVENT_DATE)
                .join(EVENT_DATE_VOTE).on(EVENT_DATE_VOTE.EVENT_DATE_ID.eq(EVENT_DATE.ID))
                .where(whereClause)
                .groupBy(EVENT_DATE.PROPOSED_DATE, EVENT_DATE.ID)
                .fetch()
                .into(EventDate.class);

        // Finally, query the proposed dates with the ID's from the previous result
        Map<EventDate, List<EventDateVote>> res = dslContext
                .select(EVENT_DATE.fields())
                .select(EVENT_DATE_VOTE.fields())
                .from(EVENT_DATE)
                .join(EVENT_DATE_VOTE).on(EVENT_DATE_VOTE.EVENT_DATE_ID.eq(EVENT_DATE.ID))
                .where(EVENT_DATE.EVENT_ID.eq(eventId)
                        .and(EVENT_DATE.ID.in(suitableDates.stream().map(EventDate::getId).collect(Collectors.toList()))))
                .fetchGroups(
                        r -> r.into(EVENT_DATE).into(EventDate.class),
                        r -> r.into(EVENT_DATE_VOTE).into(EventDateVote.class));

        return res;
    }
}
