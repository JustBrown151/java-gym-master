package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;
    private final Map<Coach, Integer> coachesCounter;

    public Timetable() {
        timetable = new HashMap<>();
        coachesCounter = new HashMap<>();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            timetable.put(dayOfWeek, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> day = timetable.get(dayOfWeek);

        List<TrainingSession> time = day.get(timeOfDay);
        if (time == null) {
            time = new ArrayList<>();
            day.put(timeOfDay, time);
        }
        time.add(trainingSession);

        Coach currentCoach = trainingSession.getCoach();
        coachesCounter.put(currentCoach, coachesCounter.getOrDefault(currentCoach, 0) + 1);
    }

    public TreeMap<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> trainingsForDay = timetable.get(dayOfWeek);
        if (trainingsForDay == null) {
            return new TreeMap<>();
        }

        return trainingsForDay;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        if (sessionsForDay.isEmpty()) {
            return Collections.emptyList();
        }
        List<TrainingSession> sessionsForTime = sessionsForDay.get(timeOfDay);
        if (sessionsForTime == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(sessionsForTime);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        if (coachesCounter.isEmpty()) {
            return Collections.emptyList();
        }

        List<CounterOfTrainings> counterOfTrainingsList = new ArrayList<>();
        for (Coach coach: coachesCounter.keySet()) {
            CounterOfTrainings counterOfTrainings = new CounterOfTrainings(coach, coachesCounter.get(coach));
            counterOfTrainingsList.add(counterOfTrainings);
        }

        counterOfTrainingsList.sort(Comparator.comparingInt(CounterOfTrainings::getCount).reversed());
        return counterOfTrainingsList;
    }
}
