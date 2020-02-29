package com.clsaa.dop.server.code.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author wsy
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User{


    private String username;
    private String access_token;

}
