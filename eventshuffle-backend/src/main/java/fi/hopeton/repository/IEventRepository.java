package fi.hopeton.repository;

import fi.hopeton.jooq.tables.pojos.EventDate;
import fi.hopeton.jooq.tables.pojos.EventDateVote;
import fi.hopeton.jooq.tables.pojos.Events;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IEventRepository {

    Map<EventDate, List<EventDateVote>> findVotedDates(Long eventId);

    Events findById(Long id);

    void insertEventVotes(Events events, String votingPerson, List<LocalDate> votedDates);

    Long insertEvent(String eventName, List<LocalDate> proposedDate);

    List<Events> findAllEvents();

    Map<EventDate, List<EventDateVote>> findSuitableDates(Long eventId);

}
