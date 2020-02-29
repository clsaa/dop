package com.clsaa.dop.server.test.model.po;

import java.time.LocalDateTime;

/**
 * @author xihao
 * @version 1.0
 * @since 29/03/2019
 */
public interface Po {

    Long getId();

    void setId(Long id);

    LocalDateTime getCtime();

    void setCtime(LocalDateTime ctime);

    LocalDateTime getMtime();

    void setMtime(LocalDateTime mtime);

    Long getCuser();

    void setCuser(Long cuser);

    Long getMuser();

    void setMuser(Long muser);

    boolean isDeleted();

    void setDeleted(boolean deleted);
}
