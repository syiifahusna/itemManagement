package com.itemManagement.entity;

import com.itemManagement.audit.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "images")
public class Image extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imgName;

    public Image(){}

    public Image(Long id, String imgName) {
        this.id = id;
        this.imgName = imgName;
    }

    public Image(String imgName) {
        this.imgName = imgName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

}
