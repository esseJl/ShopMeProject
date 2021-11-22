package com.example.shopme.common.model.entity.media;

import com.example.shopme.common.annotation.valid.ValidImageFile;
import com.example.shopme.common.model.entity.media.type.support.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "media")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class MediaAbstract implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 36)
    @Size(min = 36, max = 36)
    private String mediaUUID;

    @Column(nullable = false, length = 150)
    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @Transient
    @ValidImageFile
    private MultipartFile multipartFile;


    //NoArgConstructor


    public MediaAbstract() {
    }

    public MediaAbstract(String mediaUUID, String name, MediaType mediaType, MultipartFile multipartFile) {
        this.mediaUUID = mediaUUID;
        this.name = name;
        this.mediaType = mediaType;
        this.multipartFile = multipartFile;
    }

    //Getter
    public Long getId() {
        return id;
    }

    public String getMediaUUID() {
        return mediaUUID;
    }

    public String getName() {
        return name;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    //Setter

    public void setId(Long id) {
        this.id = id;
    }

    public void setMediaUUID(String uuid) {
        this.mediaUUID = uuid;
    }

    public void setName(String name) {
        if (name.length() > 150) {
            name = name.substring(0, 149);
        }
        this.name = name;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    // path of media
    public String getPath() {
        return
                File.separator +
                        this.getMediaType() +
                        File.separator +
                        this.getMediaUUID() +
                        File.separator +
                        StringUtils.cleanPath(this.getName());
    }

    public String getDir() {
        return
                File.separator +
                        this.getMediaType() +
                        File.separator +
                        this.getMediaUUID();
    }

    @Override
    public String toString() {
        return "MediaAbstract{" +
                "id=" + id +
                ", uuid='" + mediaUUID + '\'' +
                ", name='" + name + '\'' +
                ", mediaType=" + mediaType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MediaAbstract)) return false;
        MediaAbstract that = (MediaAbstract) o;
        return id.equals(that.id) && mediaUUID.equals(that.mediaUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mediaUUID);
    }
}
