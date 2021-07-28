package ch.heigvd.digiback.business.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Score {
    private int score;
    private int total;

    @Override
    public String toString() {
        return score + "/" + total;
    }
}
