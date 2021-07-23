package ch.heigvd.digiback.business.model;

import java.sql.Date;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Stat {
    // Angle information
    private float highestAngle;
    private float angleAverage;
    private Map<Date, Float> angleEvolution; // TODO

    // Pain information
    private float painAverage;
    private Map<Date, Integer> painEvolution; // TODO

    private Map<MovementType, Stat> statByMovementType;
}
