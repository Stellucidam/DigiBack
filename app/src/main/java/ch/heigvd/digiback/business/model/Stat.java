package ch.heigvd.digiback.business.model;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Stat {
    // Angle information
    private float highestAngle;
    private float angleAverage;
    private List<Float> angleEvolution;

    // Pain information
    private float painAverage;
    private List<Integer> painEvolution;

    private Map<MovementType, Stat> statByMovementType;
}
