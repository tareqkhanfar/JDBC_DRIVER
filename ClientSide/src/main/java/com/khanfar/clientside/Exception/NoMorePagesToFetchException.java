package com.khanfar.clientside.Exception;

public class NoMorePagesToFetchException extends RuntimeException{

    public NoMorePagesToFetchException(String str) {
        super(str);
    }
}
