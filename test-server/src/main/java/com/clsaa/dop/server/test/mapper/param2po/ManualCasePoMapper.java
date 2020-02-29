package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.ManualCaseParam;
import com.clsaa.dop.server.test.model.po.ManualCase;
import com.clsaa.dop.server.test.model.po.TestStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;
import static java.util.stream.IntStream.range;

/**
 * @author xihao
 * @version 1.0
 * @since 19/03/2019
 */
@Component
public class ManualCasePoMapper extends AbstractCommonServiceMapper<ManualCaseParam, ManualCase> {

    @Autowired
    private TestStepPoMapper testStepPoMapper;

    @Override
    public Class<ManualCaseParam> getSourceClass() {
        return ManualCaseParam.class;
    }

    @Override
    public Class<ManualCase> getTargetClass() {
        return ManualCase.class;
    }

    @Override
    public Optional<ManualCase> convert(ManualCaseParam manualCaseParam) {
        List<TestStep> testSteps = testStepPoMapper.convert(manualCaseParam.getTestSteps());
        range(0, testSteps.size())
            .forEach(i -> setIndex(testSteps, i));
        return super.convert(manualCaseParam).map(dateAndUser())
                .map(po -> {
                        testSteps.forEach(testStep -> testStep.setManualCase(po));
                        po.setTestSteps(testSteps);
                        return po;
                    });
    }

    private TestStep setIndex(List<TestStep> testSteps, int i) {
        TestStep testStep = testSteps.get(i);
        testStep.setStepIndex(i);
        return testStep;
    }
}
