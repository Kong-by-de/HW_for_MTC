package com.mipt.aleksandrivanovich.first_sem.Hw_6.Hw_11;

public class BankAccount {
  private int balance;
  private final int id;

  public BankAccount(int id, int initialBalance) {
    this.id = id;
    this.balance = initialBalance;
  }

  public int getId() {
    return id;
  }

  public int getBalance() {
    return balance;
  }

  public void withdraw(int amount) {
    if (amount > balance) {
      throw new IllegalArgumentException("Недостаточно средств");
    }
    balance -= amount;
  }

  public void deposit(int amount) {
    balance += amount;
  }
}