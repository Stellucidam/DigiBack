package ch.heigvd.digiback.business.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Quiz {
    private Long idQuiz;
    private String title;
}
