package com.mipt.aleksandrivanovich.Hw_7;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CustomArrayList<A> implements CustomList<A>, Iterable<A> {
  private static final int INITIAL_CAPACITY = 10;
  private Object[] elements;
  private int size;

  public CustomArrayList() {
    this.elements = new Object[INITIAL_CAPACITY];
    this.size = 0;
  }

  @Override
  public void add(A element) {
    if (element == null) {
      throw new IllegalArgumentException("Элемент не может быть null");
    }
    ensureCapacity();
    elements[size++] = element;
  }

  @Override
  public A get(int index) {
    checkIndex(index);
    return (A) elements[index];
  }

  @Override
  public A remove(int index) {
    checkIndex(index);
    A removed = (A) elements[index];

    System.arraycopy(elements, index + 1, elements, index, size - index - 1);
    elements[--size] = null; // Очищаем последний элемент

    return removed;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  private void ensureCapacity() {
    if (size == elements.length) {
      int newCapacity = (int) (elements.length * 1.5);
      Object[] newElements = new Object[newCapacity];
      System.arraycopy(elements, 0, newElements, 0, size);
      elements = newElements;
    }
  }

  private void checkIndex(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Индекс " + index + ", размер " + size);
    }
  }


  @Override
  public Iterator<A> iterator() {
    return new CustomIterator();
  }

  private class CustomIterator implements Iterator<A> {
    private int currentIndex = 0;

    @Override
    public boolean hasNext() {
      return currentIndex < size;
    }

    @Override
    public A next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      return (A) elements[currentIndex++];
    }
  }
}