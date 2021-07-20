package ch.heigvd.digiback.business.model.tip;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Tip {
    private TipType type;
    private Float duration;
    private int repetition;
}
