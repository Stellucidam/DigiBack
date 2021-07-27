package ch.heigvd.digiback.business.model;

public enum TipType {
    WALK,
    STRETCH,
    MUSCLE,
    EXERCISE,
    MOVEMENT_EXERCISE,
    STILL_EXERCISE,
    QUIZ;

    public static TipType getType(String name) {
        switch (name) {
            case "WALK" : return WALK;
            case "STRETCH" : return STRETCH;
            case "MUSCLE" : return MUSCLE;
            case "EXERCISE" : return EXERCISE;
            case "MOVEMENT_EXERCISE" : return MOVEMENT_EXERCISE;
            case "STILL_EXERCISE" : return STILL_EXERCISE;
            case "QUIZ" : return QUIZ;
            default: throw new UnknownError();
        }
    }
}
