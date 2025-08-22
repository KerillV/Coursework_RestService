package ru.netology.controller;

import org.springframework.web.bind.annotation.*;
import ru.netology.model.ConfirmationRequest;
import ru.netology.model.TransferRequest;
import ru.netology.service.TransferService;

@CrossOrigin
@RestController
@RequestMapping("/transfer")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/")
    @CrossOrigin//(origins = {"http://localhost:5500/"})
    public Object transferMoney(@RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }

    @PostMapping("/confirmOperation")
    public Object confirmOperation(@RequestBody ConfirmationRequest request) {
        return transferService.confirmOperation(request.getOperationId(), request.getCode());
    }

}
