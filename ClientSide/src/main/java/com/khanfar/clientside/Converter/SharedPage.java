package com.khanfar.clientside.Converter;


public class SharedPage {
    private static int pageNumber =1;
    private static int pageSize =100;


    public static int getPageNumber() {
        return pageNumber;
    }

    public static void setPageNumber(int pageNumber) {
        SharedPage.pageNumber = pageNumber;
    }

    public static int getPageSize() {
        return pageSize;
    }

    public static void setPageSize(int pageSize) {
        SharedPage.pageSize = pageSize;
    }
}