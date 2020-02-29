package com.clsaa.dop.server.test.mapper;

import com.clsaa.dop.server.test.util.BeanUtils;
import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public abstract class AbstractCommonServiceMapper<SOURCE, TARGET> implements ServiceMapper<SOURCE, TARGET> {

    public abstract Class<SOURCE> getSourceClass();

    public abstract Class<TARGET> getTargetClass();

    public Optional<SOURCE> inverseConvert(TARGET target){
        return Optional.of(target)
                .map(d -> BeanUtils.convertType(d, getSourceClass()));
    }

    public List<SOURCE> inverseConvert(Collection<TARGET> targets){
        return isEmpty(targets) ?
                Lists.newArrayList() :
                targets.stream()
                        .map(this::inverseConvert)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }

    public Optional<TARGET> convert(SOURCE source){
        return Optional.of(source).map(p -> BeanUtils.convertType(p, getTargetClass()));
    }

    public List<TARGET> convert(Collection<SOURCE> sources){
        return isEmpty(sources) ?
                Lists.newArrayList() :
                sources.stream()
                        .map(this::convert)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }
}
