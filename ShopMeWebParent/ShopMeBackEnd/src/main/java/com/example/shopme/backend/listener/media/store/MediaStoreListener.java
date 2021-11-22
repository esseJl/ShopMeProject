package com.example.shopme.backend.listener.media.store;

import com.example.shopme.backend.event.media.store.MediaStoreEvent;
import com.example.shopme.backend.service.media.i.IMediaService;
import com.example.shopme.backend.service.storage.i.IStorageService;
import com.example.shopme.common.model.entity.media.MediaAbstract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MediaStoreListener implements ApplicationListener<MediaStoreEvent> {

    private final IStorageService iStorageService;
    private final IMediaService iMediaService;


    @Autowired
    public MediaStoreListener(IStorageService iStorageService, IMediaService iMediaService) {
        this.iStorageService = iStorageService;
        this.iMediaService = iMediaService;
    }


    @Override
    public void onApplicationEvent(MediaStoreEvent event) {
        storeMedia(event);
    }

    private void storeMedia(MediaStoreEvent event){

        MediaAbstract media = event.getMedia();
        MultipartFile multipartFile = event.getMultipartFile();
    }

}
