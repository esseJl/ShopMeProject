package com.example.shopme.backend.repository.media.image;

import com.example.shopme.common.model.entity.media.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByMediaUUID(String uuid);

    void deleteByMediaUUID(String uuid);

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    @Override
    void delete(Image image);
}
