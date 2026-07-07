package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    @DisplayName("Проверка получения тренировок за определенный день")
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TimeOfDay timeOfDay = new TimeOfDay(13, 0);
        TrainingSession singleTrainingSession = new TrainingSession(group, coach, DayOfWeek.MONDAY, timeOfDay);

        timetable.addNewTrainingSession(singleTrainingSession);

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions = timetable
                .getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.get(timeOfDay).size());
        Assertions.assertEquals(singleTrainingSession, mondaySessions
                .get(new TimeOfDay(13, 0))
                .getFirst());

        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable
                .getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    @DisplayName("Проверка получения нескольких тренировок в один день в разное время")
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TimeOfDay adultThursdayTime = new TimeOfDay(20, 0);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach, DayOfWeek.THURSDAY,
                adultThursdayTime);

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TimeOfDay childTime = new TimeOfDay(13, 0);
        TimeOfDay childSaturdayTime = new TimeOfDay(10, 0);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.MONDAY,
                childTime);
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.THURSDAY,
                childTime);
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach, DayOfWeek.SATURDAY,
                childSaturdayTime);

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        TreeMap<TimeOfDay, List<TrainingSession>> mondaySessions = timetable
                .getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.get(childTime).size());
        Assertions.assertEquals(mondayChildTrainingSession, mondaySessions
                .get(childTime)
                .getFirst());

        TreeMap<TimeOfDay, List<TrainingSession>> thursdaySessions = timetable
                .getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        int countThursdaySessions = 0;
        for (TimeOfDay timeOfDay : thursdaySessions.navigableKeySet()) {
            countThursdaySessions += thursdaySessions.get(timeOfDay).size();
        }
        Assertions.assertEquals(2, countThursdaySessions);
        Assertions.assertEquals(thursdayChildTrainingSession, thursdaySessions.get(childTime).get(0));
        Assertions.assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(adultThursdayTime).get(0));

        TreeMap<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable
                .getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    @DisplayName("Проверка получения тренировок за определенный день и время")
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach, DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        List<TrainingSession> mondaySessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(singleTrainingSession, mondaySessions.get(0));

        List<TrainingSession> mondayLaterSessions = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));
        Assertions.assertTrue(mondayLaterSessions.isEmpty());
    }

    @Test
    @DisplayName("Проверка получения одновременных тренировок за определенный день и время")
    void testSeveralSessionsAtTheSameTime() {
        Timetable timetable = new Timetable();

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach firstCoach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach secondCoach = new Coach("Петрова", "Анна", "Ивановна");

        TimeOfDay timeOfDay = new TimeOfDay(18, 30);
        TrainingSession childSession = new TrainingSession(groupChild, firstCoach, DayOfWeek.WEDNESDAY,
                timeOfDay);
        TrainingSession adultSession = new TrainingSession(groupAdult, secondCoach, DayOfWeek.WEDNESDAY,
                timeOfDay);

        timetable.addNewTrainingSession(childSession);
        timetable.addNewTrainingSession(adultSession);

        List<TrainingSession> sessionsForTime = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.WEDNESDAY,
                new TimeOfDay(18, 30));
        Assertions.assertEquals(2, sessionsForTime.size());
        Assertions.assertTrue(sessionsForTime.contains(childSession));
        Assertions.assertTrue(sessionsForTime.contains(adultSession));

        TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = timetable
                .getTrainingSessionsForDay(DayOfWeek.WEDNESDAY);
        Assertions.assertEquals(2, sessionsForDay.get(timeOfDay).size());
    }

    @Test
    @DisplayName("Проверка сортировки тренировок")
    void testSessionsAreSortedByStartTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        TrainingSession eveningSession = new TrainingSession(group, coach, DayOfWeek.FRIDAY,
                new TimeOfDay(19, 0));
        TrainingSession morningSession = new TrainingSession(group, coach, DayOfWeek.FRIDAY,
                new TimeOfDay(9, 30));
        TrainingSession noonSession = new TrainingSession(group, coach, DayOfWeek.FRIDAY,
                new TimeOfDay(12, 15));
        TrainingSession earlyMorningSession = new TrainingSession(group, coach, DayOfWeek.FRIDAY,
                new TimeOfDay(9, 0));

        timetable.addNewTrainingSession(eveningSession);
        timetable.addNewTrainingSession(morningSession);
        timetable.addNewTrainingSession(noonSession);
        timetable.addNewTrainingSession(earlyMorningSession);

        TreeMap<TimeOfDay, List<TrainingSession>> fridaySessionsMap = timetable
                .getTrainingSessionsForDay(DayOfWeek.FRIDAY);

        List<TrainingSession> fridaySessions = new ArrayList<>();
        for (List<TrainingSession> sessions : fridaySessionsMap.values()) {
            fridaySessions.addAll(sessions);
        }

        Assertions.assertEquals(List.of(earlyMorningSession, morningSession, noonSession, eveningSession),
                fridaySessions);
    }

    @Test
    @DisplayName("Проверка возвращения пустых списков")
    void testEmptyTimetableReturnsEmptyCollections() {
        Timetable timetable = new Timetable();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            Assertions.assertTrue(timetable.getTrainingSessionsForDay(dayOfWeek).isEmpty());
            Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(dayOfWeek,
                    new TimeOfDay(12, 0)).isEmpty());
        }
    }

    @Test
    @DisplayName("Проверка получения и сортировки тренеров")
    void testGetCountByCoachesOrderedByCountDescending() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach vasilyev = new Coach("Васильев", "Николай", "Сергеевич");
        Coach petrova = new Coach("Петрова", "Анна", "Ивановна");
        Coach sidorov = new Coach("Сидоров", "Пётр", "Алексеевич");

        timetable.addNewTrainingSession(new TrainingSession(group, sidorov, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, petrova, DayOfWeek.MONDAY,
                new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, vasilyev, DayOfWeek.TUESDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, petrova, DayOfWeek.WEDNESDAY,
                new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, petrova, DayOfWeek.FRIDAY,
                new TimeOfDay(9, 0)));

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();

        Assertions.assertEquals(3, countByCoaches.size());
        Assertions.assertEquals(petrova, countByCoaches.get(0).getCoach());
        Assertions.assertEquals(3, countByCoaches.get(0).getCount());
        Assertions.assertEquals(1, countByCoaches.get(1).getCount());
        Assertions.assertEquals(1, countByCoaches.get(2).getCount());
        Assertions.assertTrue(List.of(vasilyev, sidorov).contains(countByCoaches.get(1).getCoach()));
        Assertions.assertTrue(List.of(vasilyev, sidorov).contains(countByCoaches.get(2).getCoach()));
        Assertions.assertNotEquals(countByCoaches.get(1).getCoach(), countByCoaches.get(2).getCoach());
    }

    @Test
    @DisplayName("Проверка уникальности ФИО")
    void testGetCountByCoachesIdentifiesCoachByFullName() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach sameCoach = new Coach("Васильев", "Николай", "Сергеевич");

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, sameCoach, DayOfWeek.MONDAY,
                new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, sameCoach, DayOfWeek.SUNDAY,
                new TimeOfDay(18, 0)));

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();

        Assertions.assertEquals(1, countByCoaches.size());
        Assertions.assertEquals(coach, countByCoaches.get(0).getCoach());
        Assertions.assertEquals(3, countByCoaches.get(0).getCount());
    }

    @Test
    @DisplayName("Проверка возвращения пустого списка для тренеров")
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();

        Assertions.assertTrue(countByCoaches.isEmpty());
    }
}
