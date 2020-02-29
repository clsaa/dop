package com.clsaa.dop.server.test.doExecute.matcher;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsNot;

/**
 * @author xihao
 * @version 1.0
 * @since 31/03/2019
 */
public class ToStringMatcher<T> extends FeatureMatcher<T,String> {

    public ToStringMatcher(Matcher<? super String> toStringMatcher) {
        super(toStringMatcher, "with toString()", "toString()");
    }

    @Override
    protected String featureValueOf(T t) {
        return String.valueOf(t);
    }

    public static ToStringMatcher hasToString(String expectedToString) {
        return new ToStringMatcher(IsEqual.equalTo(expectedToString));
    }

    public static ToStringMatcher notToString(String expectedToString) {
        return new ToStringMatcher(IsNot.not(expectedToString));
    }
}
