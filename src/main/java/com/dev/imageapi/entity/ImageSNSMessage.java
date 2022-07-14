package com.dev.imageapi.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ImageSNSMessage {
    public enum ImageUploadStatus {
        SUCCESS, FAILED, PENDING
    }

    private ImageUploadStatus status;
    private ImageMetaData metaData;
}
