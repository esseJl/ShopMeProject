package com.example.shopme.backend.service.image.i;

import com.example.shopme.backend.service.storage.exception.StorageMediaFileException;
import com.example.shopme.common.model.entity.media.image.Image;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.InputStream;

public interface IImageService {


    @Transactional(Transactional.TxType.REQUIRES_NEW)
    Image store(@Valid Image image) throws StorageMediaFileException;

    Image find(String imageId) throws StorageMediaFileException;

    InputStream load(String imageId) throws FileNotFoundException, StorageMediaFileException;

    InputStream load(Image image) throws FileNotFoundException, StorageMediaFileException;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    void delete(Image image) throws StorageMediaFileException;
}
