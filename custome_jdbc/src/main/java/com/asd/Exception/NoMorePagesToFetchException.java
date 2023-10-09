package com.asd.Exception;

public class NoMorePagesToFetchException extends RuntimeException{
    public NoMorePagesToFetchException(String str ) {
        super(str);
    }
}
