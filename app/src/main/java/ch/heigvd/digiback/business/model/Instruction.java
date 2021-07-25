package ch.heigvd.digiback.business.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Instruction {
    private int position;
    private String instruction;
    private String title;
}
