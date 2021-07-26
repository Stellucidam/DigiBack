package ch.heigvd.digiback.business.model;

import java.sql.Date;
import java.util.List;

import lombok.Getter;

@Getter
public class Activity {
    private final int id;
    private final Date date;
    private final Long nbrSteps;
    private final List<Long> exercises;
    private final Long nbrQuiz;

    public Activity() {
        exercises = null;
        id = 0;
        date = null;
        nbrQuiz = null;
        nbrSteps = null;
    }

    public Activity(int id, Date date, Long nbrSteps, List<Long> exercises, Long nbrQuiz) {
        this.id = id;
        this.date = date;
        this.nbrSteps = nbrSteps;
        this.exercises = exercises;
        this.nbrQuiz = nbrQuiz;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", date=" + date +
                ", nbrSteps=" + nbrSteps +
                ", exercises=" + exercises +
                ", nbrQuiz=" + nbrQuiz +
                '}';
    }
}
