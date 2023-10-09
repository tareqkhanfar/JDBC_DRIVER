package com.asd.MetaData;

public class MetaData {
    private String DriverName ;
    private String driverVersion ;
    private String userName ;
    private String productName ;
    private String URL ;
    private String ProductVersion ;
    private String ConnectionToken ;


    public MetaData(String driverName, String driverVersion, String userName, String productName , String URL , String ProductVersion , String connectionToken) {
        DriverName = driverName;
        this.driverVersion = driverVersion;
        this.userName = userName;
        this.productName = productName;
        this.URL = URL ;
        this.ProductVersion = ProductVersion ;
        this.ConnectionToken = connectionToken ;
    }

    public String getConnectionToken() {
        return ConnectionToken;
    }

    public void setConnectionToken(String connectionToken) {
        ConnectionToken = connectionToken;
    }

    public String getProductVersion() {
        return ProductVersion;
    }

    public void setProductVersion(String productVersion) {
        ProductVersion = productVersion;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDriverName() {
        return DriverName;
    }

    public void setDriverName(String driverName) {
        DriverName = driverName;
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public void setDriverVersion(String driverVersion) {
        this.driverVersion = driverVersion;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
