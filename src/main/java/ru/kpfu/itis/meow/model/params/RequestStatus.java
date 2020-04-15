package ru.kpfu.itis.meow.model.params;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RequestStatus {
    private String text;
    private boolean isSuccess;
}
