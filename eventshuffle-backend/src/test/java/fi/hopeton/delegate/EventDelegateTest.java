package fi.hopeton.delegate;

import fi.hopeton.TestFixtures;
import fi.hopeton.eventshuffle.model.*;
import fi.hopeton.jooq.tables.pojos.EventDate;
import fi.hopeton.jooq.tables.pojos.EventDateVote;
import fi.hopeton.jooq.tables.pojos.Events;
import fi.hopeton.repository.IEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EventDelegateTest {

    private EventDelegate eventDelegate;
    private IEventRepository eventRepository;

    @BeforeEach
    void setUp() {
        this.eventRepository = mock(IEventRepository.class);
        this.eventDelegate = new EventDelegate(eventRepository);
    }

    @Test
    void eventResults() {
        Long eventId = 999L;
        String eventName = "Unit Test Party";

        Events events = mock(Events.class);
        when(events.getId()).thenReturn(999L);
        when(events.getName()).thenReturn(eventName);

        when(eventRepository.findById(eventId)).thenReturn(events);

        Map<EventDate, List<EventDateVote>> suitableDatesMap =
                TestFixtures.getEventDateVoteMap(Arrays.asList("Unit Test", "Test Unit"), LocalDate.now());

        when(eventRepository.findSuitableDates(eventId)).thenReturn(suitableDatesMap);

        ResponseEntity<EventResultResponseDto> responseEntity = eventDelegate.eventResults(eventId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(eventId, responseEntity.getBody().getId());
        assertEquals(eventName, responseEntity.getBody().getName());
        assertEquals(1, responseEntity.getBody().getSuitableDates().size());
    }

    @Test
    void eventResultsEventNotFoundShouldReturnNotFound() {
        Long eventId = 999L;

        when(eventRepository.findById(eventId)).thenReturn(null);

        ResponseEntity<EventResultResponseDto> responseEntity = eventDelegate.eventResults(eventId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void showEvent() {
        Long eventId = 999L;
        String eventName = "Unit Test Party";

        Events events = mock(Events.class);
        when(events.getId()).thenReturn(999L);
        when(events.getName()).thenReturn(eventName);

        when(eventRepository.findById(eventId)).thenReturn(events);

        Map<EventDate, List<EventDateVote>> suitableDatesMap =
                TestFixtures.getEventDateVoteMap(Arrays.asList("Unit Test", "Test Unit"), LocalDate.now());

        when(eventRepository.findVotedDates(eventId)).thenReturn(suitableDatesMap);

        ResponseEntity<EventResponseDto> responseEntity = eventDelegate.showEvent(999L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(eventName, responseEntity.getBody().getName());
        assertEquals(1, responseEntity.getBody().getVotes().size());
    }

    @Test
    void showEventEventNotFoundShouldReturnNotFound() {
        Long eventId = 999L;

        when(eventRepository.findById(eventId)).thenReturn(null);

        ResponseEntity<EventResponseDto> responseEntity = eventDelegate.showEvent(999L);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void voteEventTest() {
        Long votedEventId = 133L;
        String voterName = "Unit Test";
        String eventName = "Unit Test Party";
        LocalDate votedDate = LocalDate.now();

        List<LocalDate> votes = Collections.singletonList(votedDate);

        EventVoteDto dto = TestFixtures.getEventVoteDto(voterName, votes);

        Events events = mock(Events.class);
        when(events.getId()).thenReturn(999L);
        when(events.getName()).thenReturn(eventName);

        Map<EventDate, List<EventDateVote>> suitableDatesMap =
                TestFixtures.getEventDateVoteMap(Arrays.asList(voterName, "Test Unit"), LocalDate.now());

        when(eventRepository.findById(votedEventId)).thenReturn(events);
        when(eventRepository.findVotedDates(votedEventId)).thenReturn(suitableDatesMap);

        ResponseEntity<EventVoteResponseDto> responseEntity = this.eventDelegate.voteEvent(votedEventId, dto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(eventName, responseEntity.getBody().getName());
        assertEquals(1, responseEntity.getBody().getVotes().size());
    }

    @Test
    void voteEventEmptyNameShouldReturnBadRequest() {
        Long votedEventId = 133L;
        LocalDate votedDate = LocalDate.now();

        List<LocalDate> votes = Collections.singletonList(votedDate);

        EventVoteDto dto = TestFixtures.getEventVoteDto("", votes);

        ResponseEntity<EventVoteResponseDto> responseEntity = this.eventDelegate.voteEvent(votedEventId, dto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void voteEventNullNameShouldReturnBadRequest() {
        Long votedEventId = 133L;
        LocalDate votedDate = LocalDate.now();

        List<LocalDate> votes = Collections.singletonList(votedDate);

        EventVoteDto dto = TestFixtures.getEventVoteDto(null, votes);

        ResponseEntity<EventVoteResponseDto> responseEntity = this.eventDelegate.voteEvent(votedEventId, dto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void voteEventEmptyVotesShouldReturnBadRequest() {
        Long votedEventId = 133L;

        List<LocalDate> votes = Collections.emptyList();

        EventVoteDto dto = TestFixtures.getEventVoteDto("unit-test-name", votes);

        ResponseEntity<EventVoteResponseDto> responseEntity = this.eventDelegate.voteEvent(votedEventId, dto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void voteEventNullVotesShouldReturnBadRequest() {
        Long votedEventId = 133L;

        List<LocalDate> votes = Collections.emptyList();

        EventVoteDto dto = TestFixtures.getEventVoteDto("unit-test-name", null);

        ResponseEntity<EventVoteResponseDto> responseEntity = this.eventDelegate.voteEvent(votedEventId, dto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void createEventTest() {
        String eventName = "unit-test-event";

        ProposedEventDto dto = TestFixtures.getProposedEventDto(eventName, Collections.singletonList(LocalDate.now()));

        Long eventId = 666L;

        when(eventRepository.insertEvent(dto.getName(), dto.getDates())).thenReturn(eventId);

        ResponseEntity<EventIdDto> responseEntity = this.eventDelegate.createEvent(dto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(eventId, responseEntity.getBody().getId());
    }

    @Test
    void createEventInvalidNameShouldReturnBadRequest() {
        String eventName = "";

        ProposedEventDto dto = TestFixtures.getProposedEventDto(eventName, Collections.singletonList(LocalDate.now()));

        ResponseEntity<EventIdDto> responseEntity = this.eventDelegate.createEvent(dto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void createEventNullNameShouldReturnBadRequest() {
        ProposedEventDto dto = TestFixtures.getProposedEventDto(null, Collections.singletonList(LocalDate.now()));

        ResponseEntity<EventIdDto> responseEntity = this.eventDelegate.createEvent(dto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void createEventInvalidDatesShouldReturnBadRequest() {
        String eventName = "unit-test-event";

        ProposedEventDto dto = TestFixtures.getProposedEventDto(eventName, Collections.emptyList());

        ResponseEntity<EventIdDto> responseEntity = this.eventDelegate.createEvent(dto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void createEventNullDatesShouldReturnBadRequest() {
        String eventName = "unit-test-event";

        ProposedEventDto dto = TestFixtures.getProposedEventDto(eventName, null);

        ResponseEntity<EventIdDto> responseEntity = this.eventDelegate.createEvent(dto);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    void listAllEventsShouldReturnEmptyList() {
        when(eventRepository.findAllEvents()).thenReturn(Collections.emptyList());

        ResponseEntity<List<EventDto>> responseEntity = this.eventDelegate.listAllEvents();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody().isEmpty());
    }

    @Test
    void listAllEventsShouldReturnListOfEvents() {
        when(eventRepository.findAllEvents()).thenReturn(Arrays.asList(mock(Events.class), mock(Events.class)));

        ResponseEntity<List<EventDto>> responseEntity = this.eventDelegate.listAllEvents();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
    }
}