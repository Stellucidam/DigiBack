package ch.heigvd.digiback.business.model;

import lombok.Getter;

public enum QuestionAnswer {
    NONE(0),
    FALSE(1),
    MAYBE_FALSE(2),
    NOT_SURE(3),
    MAYBE_TRUE(4),
    TRUE(5);

    @Getter
    private final int SCORE;
    QuestionAnswer(int score) {
        SCORE = score;
    }
}
