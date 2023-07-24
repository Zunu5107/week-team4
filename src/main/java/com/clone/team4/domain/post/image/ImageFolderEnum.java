package com.clone.team4.domain.post.image;

public enum ImageFolderEnum {

    POST("post"), PROFILE("profile");

    private final String folderName;

    ImageFolderEnum(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
}
