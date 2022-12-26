package me.tetiana.dto;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TaskData {
    @NonNull
    String content;

    Boolean isComplete = false;
}
