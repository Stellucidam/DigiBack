package ch.heigvd.digiback.business.model;

import java.sql.Date;

import lombok.Getter;

@Getter
public class Activity {
    private final int id;
    private final Date date;
    private final Long nbrSteps;
    private final Long nbrExercises;
    private final Long nbrQuiz;

    public Activity() {
        nbrExercises = null;
        id = 0;
        date = null;
        nbrQuiz = null;
        nbrSteps = null;
    }

    public Activity(int id, Date date, Long nbrSteps, Long nbrExercises, Long nbrQuiz) {
        this.id = id;
        this.date = date;
        this.nbrSteps = nbrSteps;
        this.nbrExercises = nbrExercises;
        this.nbrQuiz = nbrQuiz;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", date=" + date +
                ", nbrSteps=" + nbrSteps +
                ", nbrExercises=" + nbrExercises +
                ", nbrQuiz=" + nbrQuiz +
                '}';
    }
}
