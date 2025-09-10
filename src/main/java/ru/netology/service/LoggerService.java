package ru.netology.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.time.LocalDateTime;

@Service
public class LoggerService {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public void logTransfer(String cardFrom, String cardTo, int amount, String result, String operationId) {
        try (FileWriter writer = new FileWriter("/app/logs/log.txt", true)) {
            this.timestamp = LocalDateTime.now(); // Установка текущего времени

            double commission = amount * 0.01;

            writer.write(timestamp.toString() + ", " +
                    cardFrom + ", " +
                    cardTo + ", " +
                    amount + ", " +
                    commission + ", " +
                    result + ", " +
                    operationId + "\n");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
