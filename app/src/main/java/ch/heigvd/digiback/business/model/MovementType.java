package ch.heigvd.digiback.business.model;

import java.util.LinkedList;
import java.util.List;

public enum MovementType {
    NONE,
    FRONT_TILT,
    BACK_TILT,
    LEFT_TILT,
    RIGHT_TILT,
    RIGHT_ROTATION,
    LEFT_ROTATION;

    @Override
    public String toString() {
        switch(this) {
            case NONE: return "NONE";
            case FRONT_TILT: return "FRONT_TILT";
            case BACK_TILT: return "BACK_TILT";
            case LEFT_TILT: return "LEFT_TILT";
            case RIGHT_TILT: return "RIGHT_TILT";
            case RIGHT_ROTATION: return "RIGHT_ROTATION";
            case LEFT_ROTATION: return "LEFT_ROTATION";
            default: throw new IllegalArgumentException();
        }
    }

    public static List<MovementType> getTypes() {
        List<MovementType> list = new LinkedList<>();
        list.add(NONE);
        list.add(FRONT_TILT);
        list.add(BACK_TILT);
        list.add(LEFT_TILT);
        list.add(RIGHT_TILT);
        list.add(RIGHT_ROTATION);
        return list;
    }
}
