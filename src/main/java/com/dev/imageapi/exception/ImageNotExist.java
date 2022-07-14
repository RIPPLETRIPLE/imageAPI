package com.dev.imageapi.exception;

public class ImageNotExist extends RuntimeException {
    public ImageNotExist(String message) {
        super(message);
    }
}
