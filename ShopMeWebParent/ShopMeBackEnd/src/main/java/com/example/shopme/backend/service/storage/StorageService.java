package com.example.shopme.backend.service.storage;

import com.example.shopme.backend.service.storage.exception.StorageMediaFileException;
import com.example.shopme.backend.service.storage.i.IStorageService;
import com.example.shopme.common.model.entity.media.MediaAbstract;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageService implements IStorageService {

    private final String root = "medias";
    private final String currentPath = System.getProperty("user.dir");

    @Override
    public MediaAbstract store(MediaAbstract media) throws StorageMediaFileException {


        if (media == null || media.getMediaType() == null || media.getMediaUUID() == null || media.getName() == null) {
            throw new StorageMediaFileException("could not store this media :" + media);
        }

        MultipartFile multipartFile = media.getMultipartFile();

        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new StorageMediaFileException("multipart file is empty :");
        }

        String path = root +
                media.getPath();

        try {

            Path copyLocation = Paths.get(path);

            if (!Files.exists(copyLocation)) {
                File pathAsFile = new File(path);
                pathAsFile.mkdirs();
            }

            Files.copy(multipartFile.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            return media;
        } catch (Exception e) {

            throw new StorageMediaFileException(e.getMessage());
        }

    }

    @Override
    public void delete(MediaAbstract media) throws StorageMediaFileException {

        String path = currentPath + File.separator + root + media.getPath();
        String dir = currentPath + File.separator + root + media.getDir();

        //delete file
        Path deleteFilePath = Paths.get(path);
        if (!Files.exists(deleteFilePath)) {
            throw new StorageMediaFileException("could not find any media with this path to delete: \n" + media.getPath());
        }

        File file = new File(path);

        if (!file.exists() || !file.isFile()) {
            throw new StorageMediaFileException("file not exist:" + file);
        }

        if (!file.delete()) {
            throw new StorageMediaFileException("could not delete file ->:" + file);
        }

        //delete directory
        Path deleteDirPath = Paths.get(dir);
        if (!Files.exists(deleteDirPath)) {
            throw new StorageMediaFileException("could not find any media with this path to delete: \n" + media.getPath());
        }
        File directory = new File(dir);
        if ((!Files.exists(deleteDirPath)) || (!Files.isDirectory(deleteDirPath))) {
            throw new StorageMediaFileException("path is not a directory \n" + media.getDir());
        }
        if (!directory.delete()) {
            throw new StorageMediaFileException("could not to delete directory:" + dir);
        }
    }

    @Override
    public InputStream loadAsInputStream(MediaAbstract media) throws StorageMediaFileException, FileNotFoundException {

        String path = currentPath + File.separator + root + media.getPath();
        Path pathMediaFile = Paths.get(path);
        if (!Files.exists(pathMediaFile)) {
            throw new StorageMediaFileException("could not load inputStream of file with path:" +
                    pathMediaFile.toString() +
                    "\n for Media ->" + media
            );
        }
        File file = new File(path);
        if (!file.exists() || !file.isFile())
            throw new FileNotFoundException();

        return new FileInputStream(file);
    }


}
