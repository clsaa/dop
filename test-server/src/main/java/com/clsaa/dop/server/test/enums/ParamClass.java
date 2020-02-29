package com.clsaa.dop.server.test.enums;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
public enum  ParamClass {

    GET_PARAM("get param"),
    PATH_PARAM("path param"),
    FILE_PARAM("file param"),
    BODY_PARAM("body param")
    ;

    private String comment;

    ParamClass(String comment) {
        this.comment = comment;
    }

    public static ParamClass from(String origin) {
        switch (origin) {
            case "path":
                return PATH_PARAM;
            case "file":
                return FILE_PARAM;
            case "query":
                return GET_PARAM;
            case "body":
                return BODY_PARAM;

            default:
                return BODY_PARAM;
        }
    }
}
