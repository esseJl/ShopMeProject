package com.example.shopme.common.model.entity.media.image;

import com.example.shopme.common.model.entity.media.MediaAbstract;
import com.example.shopme.common.model.entity.media.type.support.MediaType;
import com.example.shopme.common.model.entity.user.User;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Image extends MediaAbstract {

    public Image() {
    }

    public Image(String uuid, String name, MultipartFile multipartFile) {

        super(uuid, name, MediaType.IMAGE, multipartFile);
    }

    @OneToOne(mappedBy = "image")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
