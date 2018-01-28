package com.example.dell.socialmediaapp;

/**
 * Created by DELL on 28-Jan-18.
 */

public class InstaItem {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public InstaItem() {

    }

    public InstaItem(String title, String desc, String image, String username, String userimage) {

        this.title = title;
        this.desc = desc;
        this.image = image;
        this.username = username;
        this.userimage = userimage;
    }

    private String title,desc,image,username,userimage;


}
