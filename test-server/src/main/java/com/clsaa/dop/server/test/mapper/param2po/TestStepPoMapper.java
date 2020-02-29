package com.clsaa.dop.server.test.mapper.param2po;

import com.clsaa.dop.server.test.mapper.AbstractCommonServiceMapper;
import com.clsaa.dop.server.test.model.param.TestStepParam;
import com.clsaa.dop.server.test.model.po.TestStep;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.clsaa.dop.server.test.manager.UserManager.dateAndUser;

/**
 * @author xihao
 * @version 1.0
 * @since 27/03/2019
 */
@Component
public class TestStepPoMapper extends AbstractCommonServiceMapper<TestStepParam, TestStep> {

    @Override
    public Class<TestStepParam> getSourceClass() {
        return TestStepParam.class;
    }

    @Override
    public Class<TestStep> getTargetClass() {
        return TestStep.class;
    }

    @Override
    public Optional<TestStep> convert(TestStepParam testStepParam) {
        return super.convert(testStepParam).map(dateAndUser());
    }
}
