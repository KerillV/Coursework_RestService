package ru.netology.model;

import lombok.Data;

@Data
public class ConfirmationResponse {
    private String operationId;

    // Новый конструктор с одним аргументом
    public ConfirmationResponse(String operationId) {
        this.operationId = operationId;
    }
}
