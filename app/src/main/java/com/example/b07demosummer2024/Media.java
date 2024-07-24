package com.example.b07demosummer2024;

import java.util.List;

//class contains media for each item, including videos and images
public class Media {
    private List<String> imagePaths;
    private List<String> videoPaths;

    public Media() {
    }

    public Media(List<String> imagePaths, List<String> videoPaths) {
        this.imagePaths = imagePaths;
        this.videoPaths = videoPaths;
    }

    // getters and setters
    public List<String> getImagePaths() { return imagePaths;}
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths;}
    public List<String> getVideoPaths() { return videoPaths;
    }
    public void setVideoPaths(List<String> videoPaths) {
        this.videoPaths = videoPaths;
    }
}