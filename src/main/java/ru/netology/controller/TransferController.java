package ru.netology.controller;

import org.springframework.web.bind.annotation.*;
import ru.netology.model.ConfirmationRequest;
import ru.netology.model.TransferRequest;
import ru.netology.model.TransferResponse;
import ru.netology.service.CardService;

@CrossOrigin
@RestController
@RequestMapping
public class TransferController {

    private final CardService cardService;

    public TransferController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping("/transfer")
    @CrossOrigin("http://serp-ya.github.io")
    public TransferResponse prepareTransfer(@RequestBody TransferRequest request) {
        return cardService.prepareTransfer(request);
    }

    @PostMapping("/confirmOperation")
    @CrossOrigin("http://serp-ya.github.io")
    public TransferResponse confirmOperation(@RequestBody ConfirmationRequest request) {
        return cardService.confirmOperation(request.getOperationId(), request.getCode());
    }
}
