package com.khanfar.Exception;

public class NoMorePagesToFetchException extends RuntimeException{
    public NoMorePagesToFetchException(String str ) {
        super(str);
    }
}
