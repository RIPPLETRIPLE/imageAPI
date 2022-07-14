package com.dev.imageapi.repository;

import com.dev.imageapi.entity.ImageMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageMetaDataRepository extends JpaRepository<ImageMetaData, Integer> {
    boolean existsByName(String imageName);
    void deleteByName(String imageName);
    ImageMetaData findByName(String imageName);
}
