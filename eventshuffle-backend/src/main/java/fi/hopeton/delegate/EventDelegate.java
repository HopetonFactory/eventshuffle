package fi.hopeton.delegate;

import fi.hopeton.eventshuffle.api.event.ApiApiDelegate;
import fi.hopeton.eventshuffle.model.*;
import fi.hopeton.helpers.CollectionUtils;
import fi.hopeton.helpers.StringUtils;
import fi.hopeton.jooq.tables.pojos.Events;
import fi.hopeton.repository.IEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

import static fi.hopeton.delegate.ResponseMapper.*;

@Service
public class EventDelegate implements ApiApiDelegate {

    private final IEventRepository eventRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass().getSimpleName());

    public EventDelegate(IEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public ResponseEntity<EventResultResponseDto> eventResults(@NonNull Long id) {
        LOGGER.debug("eventResults() with id={}", id);

        Events events = eventRepository.findById(id);

        if (events == null) {
            return ResponseEntity.notFound().build();
        }

        List<SuitableDateDto> suitableDateDtos = suitableDatesDto(eventRepository.findSuitableDates(id));

        EventResultResponseDto responseDto = eventResultResponseDto(events, suitableDateDtos);

        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<EventResponseDto> showEvent(@NonNull Long id) {

        LOGGER.debug("showEvent() with id={}", id);

        Events events = eventRepository.findById(id);

        if (events == null) {
            return ResponseEntity.notFound().build();
        }

        List<SuitableDateDto> suitableDateDtos = suitableDatesDto(eventRepository.findVotedDates(id));

        EventResponseDto responseDto = eventResponseDto(events, suitableDateDtos);

        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<EventVoteResponseDto> voteEvent(@NonNull Long id, @NonNull EventVoteDto eventVoteDto) {

        LOGGER.debug("voteEvent() with request={}", eventVoteDto.toString());

        if (StringUtils.isEmptyOrNull(eventVoteDto.getName()) || CollectionUtils.isEmptyOrNull(eventVoteDto.getVotes())) {
            return ResponseEntity.badRequest().build();
        }

        Events events = eventRepository.findById(id);

        eventRepository.insertEventVotes(events, eventVoteDto.getName(), eventVoteDto.getVotes());

        // Find the updated suitable dates
        List<SuitableDateDto> suitableDateDtos = suitableDatesDto(eventRepository.findVotedDates(id));

        // Create response DTO
        EventVoteResponseDto eventVoteResponseDto = new EventVoteResponseDto()
                .id(events.getId())
                .name(events.getName())
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

        Long eventId = eventRepository.insertEvent(proposedEventDto.getName(), proposedEventDto.getDates());

        return ResponseEntity.ok(new EventIdDto().id(eventId));
    }

    @Override
    public ResponseEntity<List<EventDto>> listAllEvents() {

        LOGGER.debug("listAllEvents()");

        List<EventDto> eventDtos = eventsDto(eventRepository.findAllEvents());

        return ResponseEntity.ok(eventDtos);
    }


}
