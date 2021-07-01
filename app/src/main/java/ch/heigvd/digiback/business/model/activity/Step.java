package ch.heigvd.digiback.business.model.activity;

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

    public Activity(Date date, Long nbrSteps, Long nbrExercises, Long nbrQuiz) {
        this.id = 0;
        this.date = date;
        this.nbrSteps = nbrSteps;
        this.nbrExercises = nbrExercises;
        this.nbrQuiz = nbrQuiz;
    }

    public Activity(int id, Date date, Long nbrSteps, Long nbrExercises, Long nbrQuiz) {
        this.id = id;
        this.date = date;
        this.nbrSteps = nbrSteps;
        this.nbrExercises = nbrExercises;
        this.nbrQuiz = nbrQuiz;
    }
}
