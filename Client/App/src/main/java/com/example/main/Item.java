package com.example.main;

import java.io.Serializable;

public class Item implements Serializable {
    private String image;
    private String name;
    private String description;
    private Boolean isFavorite;


    public Item(String image, String name, String description, Boolean isFavorite) {
        this.name = name;
        this.description = description;
        this.isFavorite = isFavorite;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getFavorite(){
        return this.isFavorite;
    }

    public void setFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
    }

}
