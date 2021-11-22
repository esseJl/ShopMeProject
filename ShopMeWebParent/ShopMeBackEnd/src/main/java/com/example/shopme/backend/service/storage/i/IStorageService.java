package com.example.shopme.backend.service.storage.i;


import com.example.shopme.backend.service.storage.exception.StorageMediaFileException;
import com.example.shopme.common.model.entity.media.MediaAbstract;

import java.io.FileNotFoundException;
import java.io.InputStream;

public interface IStorageService {

    MediaAbstract store(MediaAbstract media) throws StorageMediaFileException;

    void delete(MediaAbstract media) throws StorageMediaFileException;

    InputStream loadAsInputStream(MediaAbstract mediaAbstract) throws StorageMediaFileException, FileNotFoundException;

}
