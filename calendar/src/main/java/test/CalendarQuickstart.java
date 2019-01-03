package test;
/*
public class helloWorld {
    public static void main(String[] args) {
        System.out.println("Haaaaalllooo");

    }
}*/
import com.google.api.client.auth.oauth2.Credential;
        import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
        import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
        import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
        import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
        import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
        import com.google.api.client.http.javanet.NetHttpTransport;
        import com.google.api.client.json.JsonFactory;
        import com.google.api.client.json.jackson2.JacksonFactory;
        import com.google.api.client.util.DateTime;
        import com.google.api.client.util.store.FileDataStoreFactory;
        import com.google.api.services.calendar.Calendar;
        import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.IOException;
        import java.io.InputStream;
        import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CalendarQuickstart {
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static final String cal1ID = "cvjmlinkenheim@gmail.com";
    private static final String cal1Name = "Haupttermine";
    private static final String cal2ID = "o4rl2ukuv8br2fjk1rubq9dbrk@group.calendar.google.com";
    private static final String cal2Name = "Jugendtermine";
    private static final String cal3ID = "r9s1ulp9ah4uhhk781rq4argis@group.calendar.google.com";
    private static final String cal3Name = "Jungschartermine";

    private static final String ZIELCalenderID = "qdgean4u4jtlbvj4mhe9p2afhc@group.calendar.google.com";
    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = CalendarQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        // Build a new authorized API client service.

        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        deleteEvents(service, ZIELCalenderID);
        //terminAbrfage(service);
        eventsSchreiben(service, eventsBearbeiten(eventsLesen(service, cal1ID), cal1Name), ZIELCalenderID);
        eventsSchreiben(service, eventsBearbeiten(eventsLesen(service, cal2ID), cal2Name), ZIELCalenderID);
        eventsSchreiben(service, eventsBearbeiten(eventsLesen(service, cal3ID), cal3Name), ZIELCalenderID);

    }
    public static List<Event> terminAbrfage(Calendar service) throws IOException{
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list(ZIELCalenderID)
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .execute();
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
        return items;
    }
    public static void terminErstellen(Calendar service, Event event) throws IOException{
        /*Event event = new Event()
                .setSummary("Google I/O 2015")
                .setLocation("800 Howard St., San Francisco, CA 94103")
                .setDescription("A chance to hear more about Google's developer products.");

        DateTime startDateTime = new DateTime("2019-05-28T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2019-05-28T17:00:00-07:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("lpage@example.com"),
                new EventAttendee().setEmail("sbrin@example.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);
*/

        String calendarId = "primary";
        event = service.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }
    public static void rechteAbfrage(Calendar service) throws IOException{
        CalendarListEntry calendarListEntry = service.calendarList().get("primary").execute();


        System.out.println(calendarListEntry.getSummary());
        System.out.println(calendarListEntry.getAccessRole());
    }
    public static void clearCalendar(Calendar service) throws IOException{
        service.calendars().clear("primary").execute();
    }
    public static void deleteEvents(Calendar service, String calendarID) throws IOException{
        //Events events = service.events().list(calendarID).execute();
        Events events = service.events().list(ZIELCalenderID)
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        System.out.println();
        for(Event e:items) {
            service.events().delete(calendarID, e.getId()).execute();
            System.out.println(e.getSummary() + " gelöscht");
        }
    }
    public static List<Event> eventsLesen (Calendar service, String calendarID) throws IOException{
        DateTime now = new DateTime(System.currentTimeMillis());
        Long l = Long.valueOf(10512);
        l = l*1000000;
        DateTime then = new DateTime(System.currentTimeMillis() + l);
        Events events = service.events().list(calendarID).setTimeMin(now).setTimeMax(then).setSingleEvents(true).execute();
        List<Event> items = events.getItems();
        List<Event> neueItems = new LinkedList<Event>();
        for(Event e:items) {
            neueItems.add(eventKopieren(e));
        }
        return neueItems;
    }
    public static Event eventKopieren(Event alt) throws IOException{
        DateTime now = new DateTime(System.currentTimeMillis());
        Event neu = new Event();
        neu.setCreated(now);
        neu.setCreator(alt.getCreator());
        neu.setEnd(alt.getEnd());
        neu.setKind(alt.getKind());
        neu.setLocation(alt.getLocation());
        neu.setOrganizer(alt.getOrganizer());
        neu.setStart(alt.getStart());
        neu.setSummary(alt.getSummary());
        return neu;
    }
    public static List<Event> eventsBearbeiten (List<Event> events, String descript_calendar) throws IOException{
        for(Event ev:events) {
            ev.setDescription(descript_calendar);
        }
        return events;
    }
    public static List<Event> eventsAussortieren (List<Event> events, List<String> schlagwoerter) throws IOException {
        List<Event> aussortieren = new LinkedList<Event>();
        for(String krit:schlagwoerter) {
            for(Event e:events) {
                if(e.getSummary().contains(krit)) {
                    aussortieren.add(e);
                }
            }
        }
        for(Event x:aussortieren){
            events.remove(x);
        }
        return events;
    }
    public static void eventsSchreiben(Calendar service, List<Event> events, String calID) throws IOException{
        Events existEvents = service.events().list(calID).execute();
        ArrayList<String> aussort = new ArrayList<String>();
        aussort.add("Jungschar Jungs");
        aussort.add("Jungschar Mäd");
        aussort.add("Jugendkreis STB");
        aussort.add("Jugendkreis Touchdown");
        aussort.add("Jugendkreis Jumble");
        aussort.add("Jungenschaft Jesushunters");
        aussort.add("Mini-Treff");
        aussort.add("Mittwochsfußball");
        events = eventsAussortieren(events, aussort);
        for(Event ev:events) {
                ev.setRecurringEventId("");
                ev.setId("").setICalUID("");
                try{
                    ev.getEnd().isEmpty();
                } catch (Exception e) {
                    ev.setEnd(ev.getStart());

                }
                try{
                    ev = service.events().insert(calID, ev).execute();
                }catch (Exception e){
                    System.out.println(e.toString());
                }
                System.out.printf("Event created: %s\n", ev.getHtmlLink());
        }
    }
}
