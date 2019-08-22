package com.security.everywhere.repository;

import com.security.everywhere.model.TourImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TourImagesRepository extends JpaRepository<TourImages, Long> {
    List<TourImages> findByContentid(String contentId);
}
