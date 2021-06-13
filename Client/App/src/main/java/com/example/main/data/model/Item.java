package com.example.main.data.model;

import java.io.Serializable;

public class Item implements Serializable {
    private final ImageDescription imageDescription;
    private Boolean isFavorite;

    public Item(ImageDescription imageDescription, Boolean isFavorite) {
        this.imageDescription = imageDescription;
        this.isFavorite = isFavorite;
    }

    public Item(String image, String name, String description, Boolean isFavorite) {
        this.imageDescription = new ImageDescription(image, name, description);
        this.isFavorite = isFavorite;
    }

    public String getLabel() {
        return imageDescription.getLabel();
    }
    public String getDescription() {
        return imageDescription.getDefinition();
    }
    public String getImage() { return imageDescription.getImageBase64(); }
    public Boolean getFavorite(){
        return this.isFavorite;
    }

    public void setLabel(String name) { imageDescription.setLabel(name); }
    public void setDescription(String description) { imageDescription.setDefinition(description); }
    public void setFavorite(boolean isFavorite){
        this.isFavorite = isFavorite;
    }

    public boolean equals(Item other) {
        if (other == null) {
            return false;
        }

        // Custom equality check here.
        return getFavorite().equals(other.getFavorite())
                && getLabel().equals(other.getLabel())
                && getDescription().equals(other.getDescription())
                && getImage().equals(other.getImage());
    }
}
