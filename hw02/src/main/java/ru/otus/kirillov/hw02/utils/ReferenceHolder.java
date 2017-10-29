package ru.otus.kirillov.hw02.utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by Александр on 25.10.2017.
 */
//TODO: МОжно переписать на использование IdentityHashMap. Так будет экономичнее
public class ReferenceHolder extends AbstractSet<Object> {

    private Set<Object> referenceSet;

    public ReferenceHolder() {
        referenceSet = new HashSet<>();
    }


    @Override
    public boolean add(Object o) {
        return referenceSet.add(o);
    }

    @Override
    public boolean addAll(Collection<?> c) {
        return referenceSet.addAll(c);
    }

    @Override
    public void clear() {
        referenceSet.clear();
    }

    @Override
    public boolean contains(Object o) {
        return exactContains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return exactContainsAll(c);
    }

    @Override
    public boolean equals(Object o) {
        return referenceSet.equals(o);
    }

    @Override
    public int hashCode() {
        return referenceSet.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return referenceSet.isEmpty();
    }

    @Override
    public Iterator<Object> iterator() {
        return referenceSet.iterator();
    }

    @Override
    public boolean remove(Object o) {
        return referenceSet.remove(o);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return referenceSet.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return referenceSet.retainAll(c);
    }

    @Override
    public int size() {
        return referenceSet.size();
    }

    @Override
    public Spliterator<Object> spliterator() {
        return referenceSet.spliterator();
    }

    @Override
    public Object[] toArray() {
        return referenceSet.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return referenceSet.toArray(a);
    }

    @Override
    public Stream<Object> parallelStream() {
        return referenceSet.parallelStream();
    }

    @Override
    public boolean removeIf(Predicate<? super Object> filter) {
        return referenceSet.removeIf(filter);
    }

    @Override
    public Stream<Object> stream() {
        return referenceSet.stream();
    }

    @Override
    public void forEach(Consumer<? super Object> action) {
        referenceSet.forEach(action);
    }

    private boolean exactContains(Object o) {
        return referenceSet.stream()
                .anyMatch(o1 -> o == o1);
    }

    private boolean exactContainsAll(Collection<?> c) {
        return c.stream()
                .allMatch(o -> exactContains(o));
    }

}
