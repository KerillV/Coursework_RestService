package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.model.Amount;
import ru.netology.model.ConfirmationResponse;
import ru.netology.model.TransferRequest;
import ru.netology.model.TransferResponse;

import java.util.UUID;

@Service
public class TransferService {
    private final LoggerService loggerService;

    public TransferService(LoggerService loggerService) {
        this.loggerService = loggerService;
    }

    public TransferResponse transfer(TransferRequest request) {
        // Проверка валидных данных и создание уникального ID транзакции
        validateAmount(request.getAmount()); // добавлена проверка суммы
        UUID operationId = UUID.randomUUID();
        loggerService.logTransfer(request.getCardFromNumber(),
                request.getCardToNumber(),
                request.getAmount().getValue(),
                "Success", operationId.toString());
        return new TransferResponse(operationId.toString());
    }

    public ConfirmationResponse confirmOperation(String operationId, String code) {
        // Логика подтверждения транзакции
        if ("valid-code".equals(code)) {
            // простая проверка для примера
            loggerService.logConfirm("Success", operationId);
            return new ConfirmationResponse(operationId);
        } else {
            throw new RuntimeException("Invalid code");
        }
    }

    // Проверяем, что сумма не отрицательна
    public void validateAmount(Amount amount) {
        if (amount.getValue() <= 0) {
            throw new IllegalArgumentException("Сумма должна быть больше нуля!");
        }
    }
}
