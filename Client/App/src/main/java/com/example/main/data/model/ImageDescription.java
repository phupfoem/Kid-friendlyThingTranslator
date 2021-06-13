package com.example.main.data.model;

import java.io.Serializable;

public class ImageDescription implements Serializable {
    private final String imageBase64;

    private String label;
    private String definition;

    public ImageDescription(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public ImageDescription(String imageBase64, String label, String definition) {
        this.imageBase64 = imageBase64;
        this.label = label;
        this.definition = definition;
    }

    public String getImageBase64() {
        return imageBase64;
    }
    public String getLabel() {
        return label;
    }
    public String getDefinition() {
        return definition;
    }

    public void setLabel(String label) {
        this.label = label;
    }
    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
