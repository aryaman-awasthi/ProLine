package com.example.proline_appcode;

public class Product {
    String productName, purchasePrice, barCode, sellingPrice;
    double discount, tax, quantity;

    public Product(){

    }
    public Product(String productName, String purchasePrice, String barCode, String sellingPrice, double discount, double tax, double quantity) {
        this.productName = productName;
        this.purchasePrice = purchasePrice;
        this.barCode = barCode;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.tax = tax;
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(String purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
