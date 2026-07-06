package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new HashMap<>();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            timetable.put(dayOfWeek, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDay = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> day = timetable.get(dayOfWeek);

        if (day.containsKey(timeOfDay)) {
            List<TrainingSession> time = day.get(timeOfDay);
            time.add(trainingSession);
        } else {
            List<TrainingSession> time = new ArrayList<>();
            time.add(trainingSession);
            day.put(timeOfDay, time);
        }
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> sessionsForDay = timetable.get(dayOfWeek);
        if (sessionsForDay.isEmpty()) {
            return Collections.emptyList();
        }
        List<TrainingSession> result = new ArrayList<>();
        for (TimeOfDay timeOfDay : sessionsForDay.navigableKeySet()) {
            result.addAll(sessionsForDay.get(timeOfDay));
        }
        return result;
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
        Map<Coach, Integer> counters = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> day : timetable.values()) {
            for (List<TrainingSession> sessionsForTime : day.values()) {
                for (TrainingSession trainingSession : sessionsForTime) {
                    Coach coach = trainingSession.getCoach();
                    counters.put(coach, counters.getOrDefault(coach, 0) + 1);
                }
            }
        }

        if (counters.isEmpty()) {
            return Collections.emptyList();
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : counters.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort(Comparator.comparingInt(CounterOfTrainings::getCount).reversed());
        return result;
    }
}
