package com.clsaa.dop.server.gateway.model.bo;

import com.clsaa.dop.server.gateway.enums.ClientType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 客户端持久层对象
 * </p>
 *
 * @author 任贵杰 812022339@qq.com
 * @since 2018-12-29
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientBoV1 implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String DEFAULT_CLIENT_ID_PREFIX = "dop";
    public static final int DEFAULT_CLIENT_ID_PREFIX_LEN = 30;
    /**
     * id
     */
    private Long id;
    /**
     * 服务id(用户名),不能为空,用于唯一标识每一个客户端(client),即clientId; 在注册时填写长度为24以内的前缀,系统将在后面自动补全.
     */
    private String clientId;
    /**
     * 密码(密文),不能为空,用于指定客户端(client)的访问密匙; 在注册时必须填写
     */
    private String clientSecret;
    /**
     * 唯一标识client的名称; 在注册时必须填写
     */
    private String clientName;
    /**
     * client的描述
     */
    private String description;
    /**
     * 客户端类型,不能为空
     */
    private ClientType clientType;
    /**
     * 授权类型,不能为空,用空格分割,默认为目前支持client_credentials,refresh_token以空格分割,如:"client_credentials,refresh_token"
     */
    private String grantTypes;
    /**
     * 设定客户端的access_token的有效时间值(单位:秒), 若不设定值则使用默认的有效时间值(60 * 60 * 1, 1小时).
     */
    private Integer accessTokenValidity;
    /**
     * 设定客户端的refresh_token的有效时间值(单位:秒),若不设定值则使用默认的有效时间值(60 * 60 * 24 * 10, 10天)
     */
    private Integer refreshTokenValidity;
    /**
     * 其他信息
     */
    private String additionalInfo;
    /**
     * 外键, 指向此oauth_client的创建人
     */
    private Long cuser;
    /**
     * 外键, 指向此oauth_client的修改人
     */
    private Long muser;
    /**
     * 创建时间
     */
    private Timestamp ctime;
    /**
     * 修改时间
     */
    private Timestamp mtime;
}