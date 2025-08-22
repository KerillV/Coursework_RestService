package ru.netology.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.FileWriter;
import java.time.LocalDateTime;

@Service
public class LoggerService {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private LocalDateTime timestamp;


    public void logTransfer(String cardFrom, String cardTo, int amount, String result, String operationId) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            this.timestamp = LocalDateTime.now(); // Установка текущего времени
            writer.write(timestamp.toString() + ", " +
                    cardFrom + ", " +
                    cardTo + ", " +
                    amount + ", " +
                    "0," + // комиссия пока фиксированная
                    result + ", " +
                    operationId + "\n");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void logConfirm(String result, String operationId) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            this.timestamp = LocalDateTime.now(); // Установка текущего времени
            writer.write(timestamp.toString() + ", " +
                    "CONFIRMATION, " +
                    result + ", " +
                    operationId + "\n");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
