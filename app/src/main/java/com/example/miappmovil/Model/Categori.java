package com.example.miappmovil.Model;

public class Categori {

    private String Image;
    private String Name;

    public Categori() {

    }

    public Categori(String image, String name) {
        Image = image;
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
