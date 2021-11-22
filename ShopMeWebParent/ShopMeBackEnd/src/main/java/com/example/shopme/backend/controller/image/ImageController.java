package com.example.shopme.backend.controller.image;

import com.example.shopme.backend.service.image.i.IImageService;
import com.example.shopme.backend.service.storage.exception.StorageMediaFileException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/image")
public class ImageController {

    private final IImageService iImageService;

    @Autowired
    public ImageController(IImageService iImageService) {
        this.iImageService = iImageService;
    }

    @ResponseBody
    @RequestMapping(value = "/{imageId}", method = RequestMethod.GET,
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public void getImage(@PathVariable String imageId,
                         HttpServletResponse response) throws IOException, StorageMediaFileException {

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        InputStream load = iImageService.load(imageId);

        IOUtils.copy(load, response.getOutputStream());
    }
}
