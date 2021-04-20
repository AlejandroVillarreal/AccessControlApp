package com.example.accesscontrol.domain;

public class Result<T> {
    private final T value;
    private final Throwable error;

    public Result(T value, Throwable error) {
        this.value = value;
        this.error = error;
    }

    public T getValue() {
        return value;
    }

    public Throwable getError() {
        return error;
    }
}
