package com.example.alihandemir.androidmidterm;

public class ItemDetail {
    private String ImageUrl;
    private String Creator;
    private int Likes;

    public ItemDetail(String ImageUrl,String Creator,int Likes){
        this.ImageUrl = ImageUrl;
        this.Creator = Creator;
        this.Likes = Likes;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }
}
