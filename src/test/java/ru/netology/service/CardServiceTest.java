package ru.netology.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.model.Amount;
import ru.netology.model.Card;
import ru.netology.model.TransferRequest;
import ru.netology.model.TransferResponse;
import ru.netology.repository.CardRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private LoggerService loggerService;

    @Test
    public void shouldPrepareTransferSuccessfully() {
        // Подготовим тестовые данные
        TransferRequest request = new TransferRequest();
        request.setCardFromNumber("1234567890123456");
        request.setCardToNumber("9876543210987654");
        Amount amount = new Amount();
        amount.setValue(100);
        amount.setCurrency("RUB");
        request.setAmount(amount);

        // Мокируем карусель карт
        Card senderCard = new Card(1L, "1234567890123456", 1000);
        Card recipientCard = new Card(2L, "9876543210987654", 500);
        when(cardRepository.findByNumber("1234567890123456")).thenReturn(Optional.of(senderCard));
        when(cardRepository.findByNumber("9876543210987654")).thenReturn(Optional.of(recipientCard));

        // Выполнение
        TransferResponse response = cardService.prepareTransfer(request);

        // Утверждение результатов
        assertThat(response.getOperationId()).isNotEmpty();
    }

    @Test
    public void shouldConfirmOperationSuccessfully() {
        // Подготовим тестовые данные
        String operationId = "abc123def456";
        String verificationCode = "0000";
        Card senderCard = new Card(1L, "1234567890123456", 1000);
        Card recipientCard = new Card(2L, "9876543210987654", 500);
        Amount amount = new Amount();
        amount.setValue(100);
        amount.setCurrency("RUB");

        // Предварительно сохраняем операцию
        CardService.PendingOperation op = new CardService.PendingOperation(senderCard, recipientCard, amount);
        cardService.savePendingOperation(operationId, op);

        // Мокируем репозиторий
        doNothing().when(cardRepository).updateBalance(any(Card.class));

        // Выполнение
        TransferResponse response = cardService.confirmOperation(operationId, verificationCode);

        // Утверждение результатов
        assertThat(response.getOperationId()).isEqualTo(operationId);
        verify(cardRepository).updateBalance(senderCard);
        verify(cardRepository).updateBalance(recipientCard);
    }
}