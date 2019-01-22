package com.dubreuia.processors;

/**
 * Composite of {@link com.intellij.openapi.application.Result} and {@link com.intellij.openapi.application.RunResult}.
 */
public class Result<T> {

    private final T result;

    Result(T result) {
        this.result = result;
    }

    Result(com.intellij.openapi.application.RunResult<T> result) {
        this.result = result.getResultObject();
    }

    public T getResult() {
        return result;
    }

    @Override
    public String toString() {
        return result.toString();
    }

}
