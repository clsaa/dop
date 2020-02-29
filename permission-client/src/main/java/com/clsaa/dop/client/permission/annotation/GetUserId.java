package com.clsaa.dop.client.permission.annotation; /**
 *  权限管理的注解
 *
 * @author lzy
 *
 */
import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GetUserId {
}
