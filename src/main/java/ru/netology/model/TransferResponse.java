package ru.netology.model;

public class TransferResponse {
    private String operationId;

    // Конструктор с одним параметром
    public TransferResponse(String operationId) {
        this.operationId = operationId;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }
}
