package ch.heigvd.digiback.business.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Status {
    private String status;
    private String message;
}
