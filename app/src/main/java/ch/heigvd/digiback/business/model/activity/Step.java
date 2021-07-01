package ch.heigvd.digiback.business.model.activity;

import java.sql.Date;

import lombok.Getter;

@Getter
public class Step {
    private final Date date;
    private final Long nbrSteps;

    public Step() {
        date = null;
        nbrSteps = null;
    }

    public Step(Date date, Long nbrSteps) {
        this.date = date;
        this.nbrSteps = nbrSteps;
    }

}
