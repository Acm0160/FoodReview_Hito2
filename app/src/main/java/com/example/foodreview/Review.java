package com.example.foodreview;

public class Review {
    private long id;
    private String restaurantName;
    private int rating; // 1..5
    private String comment;
    private String cuisineType;
    private String address;
    private long createdAt;
    private long updatedAt;

    public Review(long id, String restaurantName, int rating, String comment,
                  String cuisineType, String address, long createdAt, long updatedAt) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.rating = rating;
        this.comment = comment;
        this.cuisineType = cuisineType;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getCuisineType() { return cuisineType; }
    public void setCuisineType(String cuisineType) { this.cuisineType = cuisineType; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
