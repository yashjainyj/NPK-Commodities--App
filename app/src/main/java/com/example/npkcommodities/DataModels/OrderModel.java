package com.example.npkcommodities.DataModels;

public class OrderModel {

    private String ItemId;
    private String orderDate;
    private String referenceNo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    private String userEmail;


    public OrderModel( String itemId, String orderDate, String referenceNo,String userId,String userEmail) {

        this.ItemId = itemId;
        this.orderDate = orderDate;
        this.referenceNo = referenceNo;
        this.userId =userId;
        this.userEmail=userEmail;
    }

    public OrderModel() {
    }


    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }


}
