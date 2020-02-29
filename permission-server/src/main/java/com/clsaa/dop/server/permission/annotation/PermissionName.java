package com.clsaa.dop.server.permission.annotation; /**
 *  权限管理的注解
 *
 * @author lzy
 *
 */
import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionName {
    String name() default "";
}
