package ch.heigvd.digiback.business.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Quiz {
    private Long idQuiz;
    private String title;
    private List<Question> questions;
}
