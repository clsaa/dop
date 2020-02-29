package com.clsaa.dop.server.test.model.param.update;

import com.clsaa.dop.server.test.enums.HttpMethod;
import com.clsaa.dop.server.test.enums.OperationType;
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
public class UpdatedRequestScript implements UpdateParam<Long> {

    private Long id;

    @NotNull
    private String rawUrl;

    @NotNull
    private HttpMethod httpMethod;

    private List<@Valid UpdatedRequestHeader> requestHeaders;

    private String requestBody;

    private List<@Valid UpdatedRequestCheckPoint> requestCheckPoints;

    //    @Min(1)
    private int retryTimes;

    //    @Min(1)
    private Long retryInterval;

    private List<@Valid UpdatedRequestResultParam> resultParams;

    @NotNull
    private OperationType operationType;

    private int order;
}
