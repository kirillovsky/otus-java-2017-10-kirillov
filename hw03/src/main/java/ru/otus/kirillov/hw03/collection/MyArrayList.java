package ru.otus.kirillov.hw03.collection;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Александр on 29.10.2017.
 */
public class MyArrayList<T> implements List<T> {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private static final double GROW_ARRAY_RATION = 1.0 + 9.0 / 8;

    private static final Object[] DEFAULT_EMPTY_ARRAY_VIEW = {};

    private static final int DEFAULT_CAPACITY = 10;

    private class Iter implements Iterator<T> {

        protected int currentPos = 0;
        protected int lastRet = -1;
        protected boolean isPreviousOrNextCalled = false;

        private Iter() {
        }

        @Override
        public boolean hasNext() {
            return currentPos < actualSize;
        }

        @Override
        public T next() {
            if (currentPos >= actualSize) {
                throw new NoSuchElementException();
            }
            lastRet = currentPos;
            isPreviousOrNextCalled = true;
            return getElement(currentPos++);
        }

        @Override
        public void remove() {
            if (!isPreviousOrNextCalled || lastRet == -1) {
                throw new IllegalStateException();
            }
            lastRet = -1;
            MyArrayList.this.remove(lastRet);
        }
    }

    private class LstIter extends Iter implements ListIterator<T> {

        private LstIter() {
        }

        private LstIter(int pos) {
            currentPos = pos;
        }

        @Override
        public boolean hasPrevious() {
            return currentPos > 0;
        }

        @Override
        public T previous() {
            if (currentPos <= 0) {
                throw new NoSuchElementException();
            }
            isPreviousOrNextCalled = true;
            lastRet = currentPos;
            return getElement(currentPos--);
        }

        @Override
        public int nextIndex() {
            return currentPos + 1 < size() ? currentPos + 1 : size();
        }

        @Override
        public int previousIndex() {
            return currentPos - 1;
        }

        @Override
        public void set(T t) {
            MyArrayList.this.set(lastRet, t);
            lastRet = -1;
        }

        @Override
        public void add(T t) {
            if (!isPreviousOrNextCalled || lastRet == -1) {
                throw new IllegalStateException();
            }
            MyArrayList.this.set(lastRet, t);
        }
    }

    private Object[] elements = DEFAULT_EMPTY_ARRAY_VIEW;

    private int actualSize;

    public MyArrayList() {
        ensureCapacity(DEFAULT_CAPACITY);
    }

    @Override
    public String toString() {
        return Arrays.stream(elements)
                .limit(actualSize)
                .map(Objects::toString)
                .collect(Collectors.joining(", ", "MyArrayList [", "]"));
    }

    public MyArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException(
                    String.format("%d is negative", capacity));
        }
        ensureCapacity(capacity);
    }

    @Override
    public int size() {
        return actualSize;
    }

    @Override
    public boolean isEmpty() {
        return actualSize == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iter();
    }

    @Override
    public Object[] toArray() {
        return elements.length > 0 ? Arrays.copyOf(elements, actualSize) :
                DEFAULT_EMPTY_ARRAY_VIEW;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (actualSize > a.length) {
            return (T1[]) Arrays.copyOf(elements, actualSize, a.getClass());
        }

        System.arraycopy(elements, 0, a, 0, actualSize);
        return a;
    }

    @Override
    public boolean add(T t) {
        add(actualSize, t);
        return true;
    }

    @Override
    public boolean remove(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean isChanged = false;
        for (T t : c) {
            isChanged |= add(t);
        }
        return isChanged;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        rangeCheck(index);
        if (c.isEmpty()) {
            return false;
        }
        ensureCapacity(actualSize + c.size());
        rightShift(index, c.size());
        Iterator iter = c.iterator();
        while (iter.hasNext()) {
            elements[index++] = iter.next();
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public void clear() {
        Arrays.fill(elements, 0, actualSize, null);
        actualSize = 0;
    }

    @Override
    public T get(int index) {
        rangeCheck(index);
        return getElement(index);
    }

    @Override
    public T set(int index, T element) {
        rangeCheck(index);
        T oldElement = getElement(index);
        elements[index] = element;
        return oldElement;
    }

    @Override
    public void add(int index, T element) {
        rangeCheck(index, actualSize + 1);
        ensureCapacity();
        elements[index] = element;
        actualSize++;
    }

    @Override
    public T remove(int index) {
        throw new NotImplementedException();
    }

    @Override
    public int indexOf(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new LstIter();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        rangeCheck(index);
        return new LstIter(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new NotImplementedException();
    }

    private void ensureCapacity(int expectedCapacity) {
        if (expectedCapacity > MAX_ARRAY_SIZE || expectedCapacity < 0) {
            throw new IllegalArgumentException("Try to expand list to size more than - " + MAX_ARRAY_SIZE);
        }

        if (elements.length > expectedCapacity) {
            return;
        }

        int newCapacity = Math.min(MAX_ARRAY_SIZE,
                (int) Math.round(elements.length * GROW_ARRAY_RATION));

        newCapacity = Math.max(expectedCapacity, newCapacity);

        elements = Arrays.copyOf(elements, newCapacity);
    }

    private void ensureCapacity() {
        ensureCapacity(actualSize + 1);
    }

    private void rangeCheck(int index, int max) {
        if (index < 0 || index >= max) {
            throw new IndexOutOfBoundsException(
                    String.format("Index %d is out of range. Last permission index - %d", index, max)
            );
        }
    }

    private void rangeCheck(int index) {
        rangeCheck(index, actualSize);
    }

    private T getElement(int index) {
        return (T) elements[index];
    }

    private void rightShift(int index) {
        rightShift(index, 1);
    }

    private void rightShift(int index, int count) {
        int moveElementCount = actualSize - index;
        if (moveElementCount > 0) {
            System.arraycopy(elements, index, elements, index + count, moveElementCount);
        }
    }

    private void leftShift(int index) {
        int moveElementCount = actualSize - index - 1;
        if (moveElementCount > 0) {
            System.arraycopy(elements, index - 1, elements, index, moveElementCount);
        }
    }


}
