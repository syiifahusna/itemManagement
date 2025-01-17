package com.itemManagement.controller;

import com.itemManagement.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/images")
public class ImageController {

    private final ImageUtil imageUtil;

    @Autowired
    public ImageController(ImageUtil imageUtil) {
        this.imageUtil = imageUtil;
    }

    @GetMapping("/{imgName}")
    public ResponseEntity<Resource> viewImage(@PathVariable String imgName){
        Resource imageResource = imageUtil.viewImage(imgName);

        if(imageResource == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageResource.getFilename() + "\"")
                .body(imageResource);

    }
}
