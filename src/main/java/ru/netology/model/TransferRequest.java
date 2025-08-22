package ru.netology.model;

import lombok.Data;

@Data
public class TransferRequest {
    private String cardFromNumber;
    private String cardFromValidTill;
    private String cardFromCVV;
    private String cardToNumber;
    private Amount amount;

}
