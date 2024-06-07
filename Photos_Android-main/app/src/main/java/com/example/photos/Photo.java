package com.example.photos;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;





public class Photo implements Serializable{
    private static final long serialVersionUID = 1L;

    public static ArrayList<Photo> allPhotos;


    private String path;

    private ArrayList<String> tags;

    private String caption;

    static String currentDir = System.getProperty("user.dir");
    static String storageDir = currentDir+"/src/photos/local";

    public Photo(String path) {
        this.path = path;
        this.tags = new ArrayList<>();
        this.caption = "";
    }


    public String getPath() {
        return path;
    }


    public ArrayList<String> getTags() {
        return tags;
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getName() {
        return new File(path).getName();
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void deleteTag(String tag) {
        tags.remove(tag);
    }

    public static void getAllPhotos() {

    }

    public static void findOrphan(String username) {

    }

    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        return file.delete();
    }


    public static boolean moveToTrash(String filePath) {
        Path path = Paths.get(filePath);
        try {
            // Try to move to trash (works on macOS and Linux)
            Files.move(path, Paths.get(System.getProperty("user.home"), ".Trash", path.getFileName().toString()));
            return true;
        } catch (Exception e) {
            // If moving to trash fails, try to move to recycle bin (Windows only)
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                Path recycleBinPath = Paths.get(System.getProperty("user.home"), "AppData", "Local", "Microsoft", "Windows", "Recycle Bin");
                try {
                    Files.move(path, Paths.get(recycleBinPath.toString(), path.getFileName().toString()));
                    return true;
                } catch (Exception ex) {
                    // If moving to recycle bin fails, delete the file
                    boolean deleteSuccessful = deleteFile(filePath);
                    return deleteSuccessful;
                }
            } else {
                // If not on Windows, delete the file
                boolean deleteSuccessful = deleteFile(filePath);
                return deleteSuccessful;
            }
        }
    }


}
