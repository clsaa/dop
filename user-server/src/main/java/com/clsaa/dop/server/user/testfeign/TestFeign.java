package com.clsaa.dop.server.user.testfeign;

import com.clsaa.dop.server.user.model.po.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestFeign {
    @Autowired
    private UserFeign userFeign;

    @GetMapping("/test")
    public Object test() {
        Object o = this.userFeign.searchUserByOrganizationIdAndEmailOrPassword(null, null, 1, 10);
        return o;
    }

    @GetMapping("/test2")
    public Object test2() {
        Object o = this.userFeign.getUserCredentialV1ByUserId(50L, UserCredential.Type.DOP_INNER_HARBOR_LOGIN_EMAIL);
        return o;
    }
}
