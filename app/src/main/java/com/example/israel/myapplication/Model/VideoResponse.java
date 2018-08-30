package com.example.israel.myapplication.Model;

public class VideoResponse {
    String videoUrl;

    public VideoResponse() {

    }

    public VideoResponse(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
