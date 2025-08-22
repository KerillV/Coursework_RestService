package ru.netology.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.model.Amount;
import ru.netology.model.TransferRequest;
import ru.netology.model.TransferResponse;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private LoggerService loggerService;

    @Test
    public void positiveAmountTest() {
        // подготовка
        Amount validAmount = new Amount();
        validAmount.setValue(100); // Положительное значение

        // выполнение и проверка
        transferService.validateAmount(validAmount); // Ничто не должно выбросить исключение
    }

    @Test
    public void zeroAmountTest() {
        // подготовка
        Amount invalidAmount = new Amount();
        invalidAmount.setValue(0); // Нулевое значение

        // проверка
        assertThatThrownBy(() -> transferService.validateAmount(invalidAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Сумма должна быть больше нуля!");
    }

    @Test
    public void negativeAmountTest() {
        // подготовка
        Amount invalidAmount = new Amount();
        invalidAmount.setValue(-50); // Отрицательное значение

        // проверка
        assertThatThrownBy(() -> transferService.validateAmount(invalidAmount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Сумма должна быть больше нуля!");
    }

    @Test
    public void shouldCreateAndLogNewTransfer() {
        // подготовка
        MockitoAnnotations.openMocks(this);
        TransferRequest request = new TransferRequest();
        request.setCardFromNumber("1234567890");
        request.setCardToNumber("9876543210");
        Amount amount = new Amount();
        amount.setValue(100);
        amount.setCurrency("RUB");
        request.setAmount(amount);

        TransferResponse response = transferService.transfer(request);

        assertThat(response.getOperationId()).isNotNull();
    }
}