package com.example.foodreview;

public class Review {
    private long id;
    private String restaurantName;
    private int rating; // 1..5
    private String comment;
    private String cuisineType;
    private String address;

    // Hito 2
    private String photoUri;   // content://... o file uri string
    private String audioPath;  // ruta del fichero (filesDir/audio/...)
    private Double lat;        // puede ser null
    private Double lng;        // puede ser null

    private long createdAt;
    private long updatedAt;

    public Review(long id, String restaurantName, int rating, String comment,
                  String cuisineType, String address,
                  String photoUri, String audioPath, Double lat, Double lng,
                  long createdAt, long updatedAt) {
        this.id = id;
        this.restaurantName = restaurantName;
        this.rating = rating;
        this.comment = comment;
        this.cuisineType = cuisineType;
        this.address = address;
        this.photoUri = photoUri;
        this.audioPath = audioPath;
        this.lat = lat;
        this.lng = lng;
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

    public String getPhotoUri() { return photoUri; }
    public void setPhotoUri(String photoUri) { this.photoUri = photoUri; }

    public String getAudioPath() { return audioPath; }
    public void setAudioPath(String audioPath) { this.audioPath = audioPath; }

    public Double getLat() { return lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLng() { return lng; }
    public void setLng(Double lng) { this.lng = lng; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}
