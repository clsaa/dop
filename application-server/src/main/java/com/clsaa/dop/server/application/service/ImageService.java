package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.ImageFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "ImageService")
public class ImageService {
    @Autowired
    private ImageFeign imageFeign;

    public void createProject(String projectName, String projectStatus, Long userId) {
        this.imageFeign.addProject(projectName, projectStatus, userId);
    }

    public List<String> getImageUrls(Long userId) {
        return this.imageFeign.getRepoAddress(userId);
    }
}
