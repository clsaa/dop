package com.clsaa.dop.server.pipeline.model.po;


import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.ArrayList;

/**
 * 流水线阶段持久层对象
 * @author 张富利
 * @since 2019-03-09
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stage {


    /**
     * 流水线阶段名称
     */
    @SerializedName("name")
    private String name;

    /**
     * 流水线阶段要做的任务
     */
    @SerializedName("steps")
    private ArrayList<Step> steps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }
}
