/*
 * Copyright (c) 2022.
 *
 * @author Henrik Toivakka
 * @contact htoivakka@gmail.com
 */

package fi.hopeton.delegate;

import fi.hopeton.eventshuffle.api.event.ApiApiDelegate;
import fi.hopeton.eventshuffle.model.Event;
import fi.hopeton.eventshuffle.model.EventId;
import fi.hopeton.eventshuffle.model.ProposedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EventDelegate implements ApiApiDelegate {

    @Override
    public ResponseEntity<EventId> createEvent(ProposedEvent proposedEvent) {
        return ResponseEntity.ok(new EventId().id(0L));
    }

    @Override
    public ResponseEntity<List<Event>> listAllEvents() {
        return ResponseEntity.ok(Collections.emptyList());
    }
}
