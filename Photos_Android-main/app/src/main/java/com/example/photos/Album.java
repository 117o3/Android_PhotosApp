package com.example.photos;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Album implements Serializable{
    public static ArrayList<Album> albums;
    private static final long serialVersionUID = 1L;

    private String albumName;
    private ArrayList<Photo> photos;


    public Album(String albumName) {
        this.albumName = albumName;
        this.photos = new ArrayList<>();
    }

    // getter method for album name
    public String getAlbumName() {
        return albumName;
    }

    // setter method for album name
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    // getter method for album photos
    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    // method to add photo to album instance
    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    // method to remove phot from album instance
    public void removePhoto(Photo photo) {
        photos.remove(photo);
    }

    // getter method for number of photos in the album
    public int getNumPhotos() {
        return photos.size();
    }

}
