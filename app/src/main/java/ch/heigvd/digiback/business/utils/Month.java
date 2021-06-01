package ch.heigvd.digiback.business.utils;

public enum Month {
    JAN(0),
    FEB(1),
    MAR(2),
    APR(3),
    MAY(4),
    JUN(5),
    JUL(6),
    AUG(7),
    SEP(8),
    OCT(9),
    NOV(10),
    DEC(11);

    private final int value;

    Month(int i) {
        value = i;
    }

    public static Month getMonth(int i) {
        if (i == JAN.getValue()) {
            return JAN;
        } else if (i == FEB.getValue()) {
            return FEB;
        } else if (i == MAR.getValue()) {
            return MAR;
        } else if (i == APR.getValue()) {
            return APR;
        } else if (i == MAY.getValue()) {
            return MAY;
        } else if (i == JUN.getValue()) {
            return JUN;
        } else if (i == JUL.getValue()) {
            return JUL;
        } else if (i == AUG.getValue()) {
            return AUG;
        } else if (i == SEP.getValue()) {
            return SEP;
        } else if (i == OCT.getValue()) {
            return OCT;
        } else if (i == NOV.getValue()) {
            return NOV;
        } else if (i == DEC.getValue()) {
            return DEC;
        } else {
            return null;
        }
    }

    public String french() {
        switch (this) {
            case JAN: return "Janvier";
            case FEB: return "Février";
            case MAR: return "Mars";
            case APR: return "Avril";
            case MAY: return "Mai";
            case JUN: return "Juin";
            case JUL: return "Juillet";
            case AUG: return "Août";
            case SEP: return "Septembre";
            case OCT: return "Octobre";
            case NOV: return "Novembre";
            case DEC: return "Décembre";
            default: return "INVALIDE";
        }
    }

    public String english() {
        switch (this) {
            case JAN: return "January";
            case FEB: return "February";
            case MAR: return "March";
            case APR: return "April";
            case MAY: return "May";
            case JUN: return "June";
            case JUL: return "July";
            case AUG: return "August";
            case SEP: return "September";
            case OCT: return "October";
            case NOV: return "November";
            case DEC: return "December";
            default: return "INVALID";
        }
    }

    public int getValue() {
        return value;
    }
}
