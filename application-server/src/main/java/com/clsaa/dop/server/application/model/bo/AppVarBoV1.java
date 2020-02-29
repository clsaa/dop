package com.clsaa.dop.server.application.model.bo;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * 变量业务层对象
 *
 * @author Bowen
 * @since 2019-3-7
 **/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppVarBoV1 {

    private Long id;


    /**
     * 键
     */
    private String varKey;


    /**
     * 值
     */
    private String varValue;


}
