package com.clsaa.dop.server.test.model.param;

import com.clsaa.dop.server.test.enums.Stage;
import lombok.Data;

/**
 * @author xihao
 * @version 1.0
 * @since 04/05/2019
 */
@Data
public class ImportParam {

    private Long caseId;

    private Stage stage;
}
