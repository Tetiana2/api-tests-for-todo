package me.tetiana.dto;

import lombok.Value;

@Value(staticConstructor = "from")
public class CreateTask {
    String documentId = "unique()";

    TaskData data;
}
