package com.mipt.aleksandrivanovich.Hw_11;

public class Bank {

  public static void sendToAccountDeadlock(BankAccount from, BankAccount to, int amount) {
    synchronized (from) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }

      synchronized (to) {
        if (from.getBalance() < amount) {
          throw new IllegalArgumentException("Недостаточно средств на счёте " + from.getId());
        }
        from.withdraw(amount);
        to.deposit(amount);
      }
    }
  }

  public static void sendToAccount(BankAccount from, BankAccount to, int amount) {
    BankAccount first = from.getId() < to.getId() ? from : to;
    BankAccount second = from.getId() < to.getId() ? to : from;

    synchronized (first) {
      synchronized (second) {
        if (from.getBalance() < amount) {
          throw new IllegalArgumentException("Недостаточно средств на счёте " + from.getId());
        }
        from.withdraw(amount);
        to.deposit(amount);
      }
    }
  }
}