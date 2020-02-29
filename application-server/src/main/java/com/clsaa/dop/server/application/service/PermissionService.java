package com.clsaa.dop.server.application.service;

import com.clsaa.dop.server.application.feign.PermissionFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "PermissionService")
public class PermissionService {
    @Autowired
    private PermissionFeign permissionFeign;

    public boolean checkPermission(String permissionName, Long userId) {

        return this.permissionFeign.permissionCheck(permissionName, userId);

    }

    public boolean check(String permissionName, Long userId, String fieldName, Long fieldValue) {
        return this.permissionFeign.check(permissionName, userId, fieldName, fieldValue);

    }

    public List<Long> getProjectMembers(String fieldName, Long fieldValue) {
        return this.permissionFeign.findUserByField(fieldName, fieldValue);
    }

    //void addRoleToUser(Long userId,Long roleId,Long loginUser){
    //   this.permissionFeign.addRoleToUser(userId,roleId,loginUser);
    //}

    public void addData(Long ruleId, Long userId, Long fieldValue, Long loginUser) {
        this.permissionFeign.addData(ruleId, userId, fieldValue, loginUser);
    }

    public List<Long> findAllIds(String permissionName, Long userId, String fieldName) {
        return this.permissionFeign.findAllIds(permissionName, userId, fieldName);
    }

    public void deleteByFieldAndUserId(Long fieldValue, String fieldName, Long userId) {
        this.permissionFeign.deleteByFieldAndUserId(fieldValue, fieldName, userId);

    }
}
