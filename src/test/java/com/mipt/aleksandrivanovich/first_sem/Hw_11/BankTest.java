package com.mipt.aleksandrivanovich.first_sem.Hw_11;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class BankTest {

  private BankAccount account1;
  private BankAccount account2;

  @BeforeEach
  void setUp() {
    account1 = new BankAccount(1, 100); // id=1, баланс=100
    account2 = new BankAccount(2, 50);  // id=2, баланс=50
  }

  @Test
  void testSendToAccountCorrect() {
    Bank.sendToAccount(account1, account2, 30);

    assertEquals(70, account1.getBalance());
    assertEquals(80, account2.getBalance());
  }

  @Test
  void testSendToAccountInsufficientFunds() {
    assertThrows(IllegalArgumentException.class, () -> {
      Bank.sendToAccount(account1, account2, 200);
    });
  }

  @Test
  void testSendToAccountDeadlock() throws InterruptedException {
    Thread thread1 = new Thread(() -> {
      Bank.sendToAccountDeadlock(account1, account2, 10);
    });

    Thread thread2 = new Thread(() -> {
      Bank.sendToAccountDeadlock(account2, account1, 10);
    });

    thread1.start();
    thread2.start();

    thread1.join(2000);
    thread2.join(2000);

    assertTrue(thread1.isAlive(), "Поток 1 должен зависнуть из-за deadlock");
    assertTrue(thread2.isAlive(), "Поток 2 должен зависнуть из-за deadlock");

    thread1.interrupt();
    thread2.interrupt();
  }

  @Test
  void testSendToAccountNoDeadlock() throws InterruptedException {
    Thread thread1 = new Thread(() -> {
      Bank.sendToAccount(account1, account2, 10);
    });

    Thread thread2 = new Thread(() -> {
      Bank.sendToAccount(account2, account1, 10);
    });

    thread1.start();
    thread2.start();

    thread1.join(2000);
    thread2.join(2000);

    assertFalse(thread1.isAlive(), "Поток 1 должен завершиться");
    assertFalse(thread2.isAlive(), "Поток 2 должен завершиться");

    assertEquals(100, account1.getBalance());
    assertEquals(50, account2.getBalance());
  }
}