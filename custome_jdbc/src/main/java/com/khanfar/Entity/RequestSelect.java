package com.khanfar.Entity;

public class RequestSelect {

  private   String query ;
    private int pageNumber ;
    private int pageSize ;
    private String token ;



    public RequestSelect(String query,String token , int pageNumber, int pageSize) {
        this.query = query;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.token = token ;
    }
    public RequestSelect(){

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getQuery() {

        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public int getOffset() {
        return (pageNumber - 1) * pageSize;
    }

}
