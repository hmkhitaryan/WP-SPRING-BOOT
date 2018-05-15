package com.egs.account.model;

import org.springframework.web.multipart.MultipartFile;

public class FileBucket {

    private MultipartFile file;

    private String comment;

    private String link;

    public String getLink() { return link; }

    public void setLink(String link) { this.link = link; }

    public MultipartFile getFile() { return file; }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}