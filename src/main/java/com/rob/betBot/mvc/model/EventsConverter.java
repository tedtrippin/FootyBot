package com.rob.betBot.mvc.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rob.betBot.Event;

/**
 * Converts {@link Event}s to {@link MvcEvent}s.
 *
 */
public class EventsConverter {

    private static EventDateComparator eventDateComparator = new EventDateComparator();

    public Collection<MvcEvent> convertEvents(Collection<Event> events) {

        List<Event> orderedEvents = new ArrayList<>(events);
        Collections.sort(orderedEvents, eventDateComparator);

        Collection<MvcEvent> mvcEvents = new ArrayList<>();
        orderedEvents.forEach(e -> mvcEvents.add(new MvcEvent(e)));
        return mvcEvents;
    }

    static class EventDateComparator implements Comparator<Event> {

        @Override
        public int compare(Event e1, Event e2) {

            long t1 = e1.getEventData().getExpectedStartTime();
            long t2 = e2.getEventData().getExpectedStartTime();

            if (t1 == t2)
                return 0;

            return t1 > t2 ? 1 : -1;
        }
    }
}
