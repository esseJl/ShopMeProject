package com.example.shopme.backend.service.image;

import com.example.shopme.backend.repository.media.image.ImageRepository;
import com.example.shopme.backend.service.image.i.IImageService;
import com.example.shopme.backend.service.storage.exception.StorageMediaFileException;
import com.example.shopme.backend.service.storage.i.IStorageService;
import com.example.shopme.common.model.entity.media.MediaAbstract;
import com.example.shopme.common.model.entity.media.image.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Optional;

@Service
@Validated
public class ImageService implements IImageService {

    private final ImageRepository imageRepository;
    private final IStorageService iStorageService;

    @Autowired
    public ImageService(ImageRepository imageRepository,
                        IStorageService iStorageService) {
        this.imageRepository = imageRepository;
        this.iStorageService = iStorageService;
    }


    @Override
    public Image store(@Valid Image image) throws StorageMediaFileException {

        MediaAbstract storeImage = iStorageService.store(image);
        Image savedImage = (Image) storeImage;

        imageRepository.save(savedImage);

        return savedImage;
    }

    @Override
    public Image find(String imageId) throws StorageMediaFileException {
        Optional<Image> imageByUUID = imageRepository.findByMediaUUID(imageId);
        return imageByUUID.orElseThrow(() ->
                new StorageMediaFileException("could not find image with imageId:" + imageId));
    }

    @Override
    public InputStream load(String imageId) throws FileNotFoundException, StorageMediaFileException {
        Image image = find(imageId);
        InputStream inputStream = iStorageService.loadAsInputStream(image);
        return inputStream;
    }

    @Override
    public InputStream load(Image image) throws FileNotFoundException, StorageMediaFileException {
        return iStorageService.loadAsInputStream(image);
    }

    
    @Override
    public void delete(Image image) throws StorageMediaFileException {
        //imageRepository.deleteByMediaUUID(image.getMediaUUID());
        imageRepository.delete(image);
        iStorageService.delete(image);
    }


}
