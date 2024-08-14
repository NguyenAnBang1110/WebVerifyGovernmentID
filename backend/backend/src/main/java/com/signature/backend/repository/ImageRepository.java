package com.signature.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.signature.backend.entity.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
