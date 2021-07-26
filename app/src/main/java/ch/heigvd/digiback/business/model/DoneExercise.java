package ch.heigvd.digiback.business.model;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

@Getter
public class DoneExercise {
    private final Date date;
    private final List<Long> idExercises;

    public DoneExercise() {
        date = null;
        idExercises = new LinkedList<>();
    }

    public DoneExercise(Date date, List<Long> idExercises) {
        this.date = date;
        this.idExercises = idExercises;
    }

}
