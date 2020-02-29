package com.clsaa.dop.server.test.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public interface ServiceMapper<SOURCE, TARGET> {

    Optional<SOURCE> inverseConvert(TARGET target);

    List<SOURCE> inverseConvert(Collection<TARGET> targets);

    Optional<TARGET> convert(SOURCE source);

    List<TARGET> convert(Collection<SOURCE> sources);
}
