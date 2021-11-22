package com.example.shopme.backend.event.media.store;

import com.example.shopme.common.model.entity.media.MediaAbstract;
import org.springframework.context.ApplicationEvent;
import org.springframework.web.multipart.MultipartFile;

public class MediaStoreEvent extends ApplicationEvent {

    private MediaAbstract media;
    private MultipartFile multipartFile;



    public MediaStoreEvent(MediaAbstract media, MultipartFile multipartFile) {
        super(media);
        this.media = media;
        this.multipartFile = multipartFile;
    }


    public MediaAbstract getMedia() {
        return media;
    }

    public void setMedia(MediaAbstract media) {
        this.media = media;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }
}
