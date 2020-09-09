package com.example.npkcommodities.DataModels;

public class Item_data_model {
//    private String shopId;
//    private String itemId;
//    private String productName;
//    private String productPrice;
//    private String productDescription;
//    private String productCategory;
//    public String getImageUrl() {
//        return url;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.url = imageUrl;
//    }
//
//    private String url;
//    public Item_data_model() {
//    }
//
//    public String getShopId() {
//        return shopId;
//    }
//
//    public void setShopId(String shopId) {
//        this.shopId = shopId;
//    }
//
//    public String getItemId() {
//        return itemId;
//    }
//
//    public void setItemId(String itemId) {
//        this.itemId = itemId;
//    }
//
//    public String getItemName() {
//        return productName;
//    }
//
//    public void setItemName(String itemName) {
//        this.productName = itemName;
//    }
//
//
//    public String getItemPrice() {
//        return productPrice;
//    }
//
//    public void setItemPrice(String itemPrice) {
//        this.productPrice = itemPrice;
//    }
//
//    public String getDescription() {
//        return productDescription;
//    }
//
//    public void setDescription(String description) {
//        this.productDescription = description;
//    }
//
//    public Item_data_model(String shopId, String itemId, String itemName, String weight, String quantity, String itemPrice, String description, String category, String imageUrl) {
//        this.shopId = shopId;
//        this.itemId = itemId;
//        this.productName = itemName;
//        this.productPrice = itemPrice;
//        this.productDescription = description;
//        this.productCategory = category;
//        this.url = imageUrl;
//    }
//
//    public String getCategory() {
//        return productCategory;
//    }
//
//    public void setCategory(String category) {
//        this.productCategory = category;
//    }
    private String key;
   private String productName;
    private int productPrice;
    private String url;
    private String productDescription;
    private String productCategory;

    public Item_data_model() {
    }

    public Item_data_model(String key, String productName, int productPrice, String url, String productDescription, String productCategory) {
        this.key = key;
        this.productName = productName;
        this.productPrice = productPrice;
        this.url = url;
        this.productDescription = productDescription;
        this.productCategory = productCategory;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
