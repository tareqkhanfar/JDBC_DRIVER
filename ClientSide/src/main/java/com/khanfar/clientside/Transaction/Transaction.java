package com.khanfar.clientside.Transaction;

import java.io.IOException;

public class Transaction {
    private String url ;
    private String Token  ;



    public Transaction (String url) {
        this.url = url ;
    }

    public Transaction() {

    }

    public String beginTransaction() throws IOException {
        this.Token = TransactionRequest.StartEndTransactionViaHttp(this.url , "N/A"  ,TransactionEnum.TOKEN_START) ;
        return this.Token  ;
    }

    public boolean commit() throws IOException {
        return TransactionRequest.TransactionHttp(this.url , this.Token , TransactionEnum.COMMIT);
    }


    public boolean rollback() throws IOException {
        return TransactionRequest.TransactionHttp(this.url ,this.Token ,  TransactionEnum.ROLLBACK);
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public void setCommitAutoAsTrue() throws IOException {
      TransactionRequest.StartEndTransactionViaHttp(this.url , this.Token ,  TransactionEnum.COMMIT_AUTO) ;
    }

    public void  closeConnection() throws IOException {
        TransactionRequest.StartEndTransactionViaHttp(this.url , this.Token , TransactionEnum.CLOSE_CONNECTION);
        this.Token = null ;
    }
}
