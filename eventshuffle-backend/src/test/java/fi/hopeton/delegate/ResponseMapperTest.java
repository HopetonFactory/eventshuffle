package fi.hopeton.delegate;

import fi.hopeton.TestFixtures;
import fi.hopeton.eventshuffle.model.EventDto;
import fi.hopeton.eventshuffle.model.EventResponseDto;
import fi.hopeton.eventshuffle.model.EventResultResponseDto;
import fi.hopeton.eventshuffle.model.SuitableDateDto;
import fi.hopeton.jooq.tables.pojos.EventDate;
import fi.hopeton.jooq.tables.pojos.EventDateVote;
import fi.hopeton.jooq.tables.pojos.Events;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseMapperTest {

    @Test
    void eventResponseDto() {
        Long eventId = 999L;
        String eventName = "unit-test-event";

        Events events = mock(Events.class);
        when(events.getName()).thenReturn(eventName);
        when(events.getId()).thenReturn(eventId);

        Map<EventDate, List<EventDateVote>> suitableDatesMap =
                TestFixtures.getEventDateVoteMap(Arrays.asList("Unit Test", "Test Unit"), LocalDate.now());

        EventResponseDto eventResponseDto =
                ResponseMapper.eventResponseDto(events, ResponseMapper.suitableDatesDto(suitableDatesMap));

        assertEquals(eventId, eventResponseDto.getId());
        assertEquals(eventName, eventResponseDto.getName());
        assertEquals(1, eventResponseDto.getVotes().size());
    }

    @Test
    void eventResultResponseDto() {
        Long eventId = 999L;
        String eventName = "unit-test-event";

        Events events = mock(Events.class);
        when(events.getName()).thenReturn(eventName);
        when(events.getId()).thenReturn(eventId);

        Map<EventDate, List<EventDateVote>> suitableDatesMap =
                TestFixtures.getEventDateVoteMap(Arrays.asList("Unit Test", "Test Unit"), LocalDate.now());

        EventResultResponseDto eventResultResponseDto =
                ResponseMapper.eventResultResponseDto(events, ResponseMapper.suitableDatesDto(suitableDatesMap));

        assertEquals(eventId, eventResultResponseDto.getId());
        assertEquals(eventName, eventResultResponseDto.getName());
        assertEquals(1, eventResultResponseDto.getSuitableDates().size());
    }

    @Test
    void suitableDatesDto() {
        LocalDate localDate = LocalDate.now();
        List<String> people = Arrays.asList("Unit Test", "Test Unit");

        Map<EventDate, List<EventDateVote>> suitableDatesMap =
                TestFixtures.getEventDateVoteMap(people, localDate);

        List<SuitableDateDto> suitableDateDtos = ResponseMapper.suitableDatesDto(suitableDatesMap);

        assertFalse(suitableDateDtos.isEmpty());
        assertEquals(localDate, suitableDateDtos.get(0).getDate());
        assertEquals(people, suitableDateDtos.get(0).getPeople());
    }

    @Test
    void eventsDto() {
        Long eventId = 999L;
        String eventName = "unit-test-event";

        Events events = mock(Events.class);
        when(events.getName()).thenReturn(eventName);
        when(events.getId()).thenReturn(eventId);

        List<EventDto> eventDtos = ResponseMapper.eventsDto(Collections.singletonList(events));

        assertFalse(eventDtos.isEmpty());
        assertEquals(eventId, eventDtos.get(0).getId());
        assertEquals(eventName, eventDtos.get(0).getName());
    }
}