package com.clsaa.dop.server.test.model.dto;

import lombok.Data;

/**
 * @author xihao
 * @version 1.0
 * @since 02/04/2019
 */
@Data
public class User {

    private Long id;

    private String name;

    private String email;

    private String avatarURL;
}
