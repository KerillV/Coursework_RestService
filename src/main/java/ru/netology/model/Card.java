package ru.netology.model;

public class Card {
    private long id;
    private String number;
    private Integer balance;

    public Card(long id, String number, Integer balance) {
        this.id = id;
        this.number = number;
        this.balance = balance;
    }

    public Card() {
    } // пустой конструктор для де-сериализации

    // Геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    // Дебет счета
    public boolean debit(Integer amount) {
        if (balance >= amount) {
            balance -= amount; // вычитаем из баланса
            return true;
        }
        return false;
    }

    // кредит счета
    public void credit(Integer amount) {
        balance += amount; // прибавляем к балансу
    }

}