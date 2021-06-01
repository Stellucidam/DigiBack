package ch.heigvd.digiback.business.utils;

public enum Day {
    SUN(0),
    MON(1),
    TUE(2),
    WED(3),
    THU(4),
    FRI(5),
    SAT(6);

    private final int value;

    Day(int i) {
        value = i;
    }

    public static Day getDay(int i) {
        if (i == MON.getValue()) {
            return MON;
        } else if (i == TUE.getValue()) {
            return TUE;
        } else if (i == WED.getValue()) {
            return WED;
        } else if (i == THU.getValue()) {
            return THU;
        } else if (i == FRI.getValue()) {
            return FRI;
        } else if (i == SAT.getValue()) {
            return SAT;
        } else if (i == SUN.getValue()) {
            return SUN;
        } else {
            return null;
        }
    }

    public String french() {
        switch (this) {
            case MON: return "Lundi";
            case TUE: return "Mardi";
            case WED: return "Mercredi";
            case THU: return "Jeudi";
            case FRI: return "Vendredi";
            case SAT: return "Samedi";
            case SUN: return "Dimanche";
            default: return "INVALIDE";
        }
    }

    public String frenchAbbreviation() {
        switch (this) {
            case MON: return "L";
            case TUE:
            case WED:
                return "M";
            case THU: return "J";
            case FRI: return "V";
            case SAT: return "S";
            case SUN: return "D";
            default: return "?";
        }
    }

    public String english() {
        switch (this) {
            case MON: return "Monday";
            case TUE: return "Tuesday";
            case WED: return "Wednesday";
            case THU: return "Thursday";
            case FRI: return "Friday";
            case SAT: return "Saturday";
            case SUN: return "Sunday";
            default: return "INVALID";
        }
    }

    public String englishAbbreviation() {
        switch (this) {
            case MON: return "M";
            case TUE:
            case THU:
                return "T";
            case WED: return "W";
            case FRI: return "F";
            case SAT:
            case SUN:
                return "S";
            default: return "?";
        }
    }

    public int getValue() {
        return value;
    }
}
