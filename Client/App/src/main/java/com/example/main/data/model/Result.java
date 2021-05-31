package com.example.main.data.model;

public class Result<T> {
    private Result(){}
    public static class Success<T> extends Result{
        private T data;
        public Success(T data){
            this.data = data;

        }

        public T getData() {
            return data;
        }
    }
    public static class Error extends Result{
        private Exception error;
        public Error(Exception error){
            this.error = error;
        }
        public Exception getError() {
            return error;
        }
    }
}