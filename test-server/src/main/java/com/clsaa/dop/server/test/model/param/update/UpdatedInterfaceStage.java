package com.clsaa.dop.server.test.model.param.update;

import com.clsaa.dop.server.test.enums.Stage;
import com.clsaa.dop.server.test.model.param.UpdateParam;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xihao
 * @version 1.0
 * @since 19/04/2019
 */
@Data
public class UpdatedInterfaceStage implements UpdateParam<Long> {

    private Long id;

    private Long caseId;

    @NotNull
    private Stage stage;

    private List<@Valid UpdatedRequestScript> requestScripts;

    private List<@Valid UpdatedWaitOperation> waitOperations;
}
