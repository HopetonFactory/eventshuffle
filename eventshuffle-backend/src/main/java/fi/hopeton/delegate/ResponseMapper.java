package fi.hopeton.delegate;

import fi.hopeton.eventshuffle.model.EventDto;
import fi.hopeton.eventshuffle.model.EventResponseDto;
import fi.hopeton.eventshuffle.model.EventResultResponseDto;
import fi.hopeton.eventshuffle.model.SuitableDateDto;
import fi.hopeton.jooq.tables.pojos.EventDate;
import fi.hopeton.jooq.tables.pojos.EventDateVote;
import fi.hopeton.jooq.tables.pojos.Events;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResponseMapper {

    public static EventResponseDto eventResponseDto(Events events, List<SuitableDateDto> suitableDateDtos) {
        EventResponseDto responseDto = new EventResponseDto();
        responseDto.setName(events.getName());
        responseDto.setId(events.getId());
        responseDto.setVotes(suitableDateDtos);

        return responseDto;
    }

    public static EventResultResponseDto eventResultResponseDto(Events events, List<SuitableDateDto> suitableDateDtos) {
        EventResultResponseDto responseDto = new EventResultResponseDto();
        responseDto.setName(events.getName());
        responseDto.setId(events.getId());
        responseDto.setSuitableDates(suitableDateDtos);

        return responseDto;
    }

    public static List<SuitableDateDto> suitableDatesDto(Map<EventDate, List<EventDateVote>> suitableDatesMap) {
        List<SuitableDateDto> suitableDateDtos = suitableDatesMap.entrySet().stream().map(res -> {
            SuitableDateDto suitableDateDto = new SuitableDateDto();
            suitableDateDto.setDate(res.getKey().getProposedDate());
            suitableDateDto.setPeople(res.getValue().stream().map(EventDateVote::getVotingPerson).collect(Collectors.toList()));

            return suitableDateDto;
        }).collect(Collectors.toList());

        return suitableDateDtos;
    }

    public static List<EventDto> eventsDto(List<Events> events) {
        return events.stream().map(event -> {
            EventDto eventDto = new EventDto();
            eventDto.setId(event.getId());
            eventDto.setName(event.getName());
            return eventDto;
        }).collect(Collectors.toList());
    }
}
