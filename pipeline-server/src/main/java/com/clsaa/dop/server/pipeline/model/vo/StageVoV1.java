package com.clsaa.dop.server.pipeline.model.vo;

import com.clsaa.dop.server.pipeline.model.po.Step;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.ArrayList;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StageVoV1 {
    private String name;

    private ArrayList<StepVoV1> steps;
}
