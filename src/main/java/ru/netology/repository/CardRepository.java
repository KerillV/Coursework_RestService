package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Card;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class CardRepository {

    // Хранение карт в мапе
    private final Map<Long, Card> cards = new HashMap<>();

    // Добавление карты в память
    public void addCard(Card card) {
        cards.put(card.getId(), card);
    }

    // Поиск карты по номеру
    public Optional<Card> findByNumber(String number) {
        return cards.values().stream()
                .filter(card -> card.getNumber().equals(number))
                .findFirst();
    }

    // Обновление баланса карты
    public void updateBalance(Card updatedCard) {
        cards.replace(updatedCard.getId(), updatedCard);
    }

    // Для демо целей: заполнение списков карт при создании репозитория
    public CardRepository() {
        // Добавляем для примера карты
        addCard(new Card(1L, "1234567890123456", 1000));
        addCard(new Card(2L, "9876543210987654", 500));
    }
}