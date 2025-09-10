package ru.netology.service;

import org.springframework.stereotype.Service;
import ru.netology.model.Amount;
import ru.netology.model.Card;
import ru.netology.model.TransferRequest;
import ru.netology.model.TransferResponse;
import ru.netology.repository.CardRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final LoggerService loggerService;
    private Map<String, PendingOperation> pendingOperations = new ConcurrentHashMap<>();

    public CardService(CardRepository cardRepository, LoggerService loggerService) {
        this.cardRepository = cardRepository;
        this.loggerService = loggerService;
    }

    // Метод для подготовки перевода
    public TransferResponse prepareTransfer(TransferRequest request) {
        // Проверка существования карт
        Optional<Card> senderCardOpt = cardRepository.findByNumber(request.getCardFromNumber());
        Optional<Card> recipientCardOpt = cardRepository.findByNumber(request.getCardToNumber());

        if (!senderCardOpt.isPresent()) {
            throw new IllegalStateException("Карта отправителя не найдена");
        }
        if (!recipientCardOpt.isPresent()) {
            throw new IllegalStateException("Карта получателя не найдена");
        }

        Card senderCard = senderCardOpt.get();
        Card recipientCard = recipientCardOpt.get();

        // Генерация уникального ID и проверочного кода
        String operationId = UUID.randomUUID().toString();
        String verificationCode = "0000"; // Постоянный проверочный код

        // Сохраняем подготовленную операцию
        PendingOperation op = new PendingOperation(senderCard, recipientCard, request.getAmount());
        pendingOperations.put(operationId, op);

        return new TransferResponse(operationId);
    }

    // Метод для подтверждения перевода
    public TransferResponse confirmOperation(String operationId, String verificationCode) {
        PendingOperation op = pendingOperations.remove(operationId);
        if (op == null || !verificationCode.equals(op.getVerificationCode())) {
            throw new IllegalStateException("Неверный код подтверждения или операция не найдена");
        }

        // Проверка баланса
        if (!op.getSenderCard().debit(op.getAmount().getValue())) {
            throw new IllegalStateException("Недостаточно средств на карте отправителя");
        }

        // Пополняем карту получателя
        op.getRecipientCard().credit(op.getAmount().getValue());

        // Сохраняем изменения в балансах карт
        cardRepository.updateBalance(op.getSenderCard());
        cardRepository.updateBalance(op.getRecipientCard());

        loggerService.logTransfer(
                op.getSenderCard().getNumber(),
                op.getRecipientCard().getNumber(),
                op.getAmount().getValue(),
                "Перевод средств",
                operationId.toString()
        );

        return new TransferResponse(operationId);
    }

    // Приватные вспомогательные методы
    private boolean hasSufficientFunds(Card senderCard, Integer amount) {
        return senderCard.getBalance() >= amount;
    }

    private void debit(Card senderCard, Integer amount) {
        senderCard.setBalance(senderCard.getBalance() - amount);
    }

    private void credit(Card recipientCard, Integer amount) {
        recipientCard.setBalance(recipientCard.getBalance() + amount);
    }

    // Внутренний класс для хранения временных данных о подготовке перевода
    public static class PendingOperation {
        private final Card senderCard;
        private final Card recipientCard;
        private final Amount amount;
        private final String verificationCode;

        public PendingOperation(Card senderCard, Card recipientCard, Amount amount) {
            this.senderCard = senderCard;
            this.recipientCard = recipientCard;
            this.amount = amount;
            this.verificationCode = "0000";
        }

        public Card getSenderCard() {
            return senderCard;
        }

        public Card getRecipientCard() {
            return recipientCard;
        }

        public Amount getAmount() {
            return amount;
        }

        public String getVerificationCode() {
            return verificationCode;
        }
    }

    // Метод для добавления операции в мапу
    public void savePendingOperation(String operationId, PendingOperation op) {
        pendingOperations.put(operationId, op);
    }
}
