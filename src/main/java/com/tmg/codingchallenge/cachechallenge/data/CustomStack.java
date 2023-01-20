package com.tmg.codingchallenge.cachechallenge.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static java.util.Objects.isNull;

public class CustomStack<T> {

    private CustomStackItem<T> topItem;

    @Getter
    private int size;

    public void pushItem(T item) {
        if (isNull(item))
            throw new IllegalArgumentException("Stack Item cannot be null!");

        this.topItem = new CustomStackItem<>(this.topItem, item);
        size++;
    }

    public T pop() {
        final CustomStackItem<T> poppedTopItem = this.topItem;
        if (isNull(poppedTopItem))
            return null;

        this.topItem = poppedTopItem.getPreviousItem();
        size--;
        return poppedTopItem.getContent();
    }

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    private static class CustomStackItem<T> {
        private CustomStackItem<T> previousItem;

        private T content;
    }

}
