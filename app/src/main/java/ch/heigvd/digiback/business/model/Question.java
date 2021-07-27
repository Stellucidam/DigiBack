package ch.heigvd.digiback.business.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Question {
    private Long idQuestion;
    private String title;
}
