package fi.hopeton;

import fi.hopeton.eventshuffle.model.EventVoteDto;
import fi.hopeton.eventshuffle.model.ProposedEventDto;
import fi.hopeton.jooq.tables.pojos.EventDate;
import fi.hopeton.jooq.tables.pojos.EventDateVote;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestFixtures {

    public static ProposedEventDto getProposedEventDto(String name, List<LocalDate> localDateList) {
        ProposedEventDto dto = new ProposedEventDto();
        dto.setDates(localDateList);
        dto.setName(name);

        return dto;
    }

    public static ProposedEventDto getProposedEventDto(String name) {
        return getProposedEventDto(name, Collections.emptyList());
    }

    public static EventVoteDto getEventVoteDto(String voterName, List<LocalDate> votes) {
        EventVoteDto dto = new EventVoteDto();
        dto.setName(voterName);
        dto.setVotes(votes);

        return dto;
    }

    public static EventVoteDto getEventVoteDto(String voterName) {
        return getEventVoteDto(voterName);
    }

    public static Map<EventDate, List<EventDateVote>> getEventDateVoteMap(List<String> votingPersons, LocalDate proposedDate) {
        Map<EventDate, List<EventDateVote>> suitableDatesMap = new HashMap<>();

        EventDate eventDate = new EventDate();
        eventDate.setEventId(999L);
        eventDate.setId(999L);
        eventDate.setProposedDate(proposedDate);

        List<EventDateVote> eventDateVotes = votingPersons.stream().map(p -> {
            EventDateVote eventDateVote = new EventDateVote();
            eventDateVote.setEventDateId(999L);
            eventDateVote.setVotingPerson(p);

            return eventDateVote;
        }).collect(Collectors.toList());

        suitableDatesMap.put(eventDate, eventDateVotes);

        return suitableDatesMap;
    }


}
