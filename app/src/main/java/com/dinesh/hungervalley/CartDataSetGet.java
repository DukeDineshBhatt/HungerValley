package com.dinesh.hungervalley;


public class CartDataSetGet {

   private String pName,price,quantity;

    public CartDataSetGet() {

    }

    public CartDataSetGet(String pName, String price, String quantity) {
        this.pName = pName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
