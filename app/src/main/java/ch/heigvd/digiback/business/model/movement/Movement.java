package ch.heigvd.digiback.business.model.movement;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import lombok.Getter;

@Getter
public class Movement {
    private final int id;
    private final MovementType type;
    private final Date date;
    private final List<Float> angles;

    public Movement() {
        type = MovementType.UNKNOWN;
        id = 0;
        date = null;
        angles = new LinkedList<>();
    }

    public Movement(int id, MovementType type, Date date, List<Float> angles) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.angles = angles;
    }

    public Movement(MovementType type, Date date, List<Float> angles) {
        this.id = 0;
        this.type = type;
        this.date = date;
        this.angles = angles;
    }
}
