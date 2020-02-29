package com.clsaa.dop.server.test.doExecute;

import com.clsaa.dop.server.test.model.dto.InterfaceStageDto;

import java.util.Comparator;

import static java.util.Objects.isNull;

/**
 * @author xihao
 * @version 1.0
 * @since 22/03/2019
 */
public class StageSorter implements Comparator<InterfaceStageDto> {

    @Override
    public int compare(InterfaceStageDto o1, InterfaceStageDto o2) {
        boolean isO1Null = isNull(o1);
        boolean isO2Null = isNull(o2);
        if (isO1Null && isO2Null) {
            return 0;
        } else if (isO1Null) {
            return -1;
        } else if (isO2Null) {
            return 1;
        }else {
            return o1.getStage().compareTo(o2.getStage());
        }
    }
}
