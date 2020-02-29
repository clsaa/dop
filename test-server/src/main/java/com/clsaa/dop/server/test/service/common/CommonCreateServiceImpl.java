package com.clsaa.dop.server.test.service.common;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.dop.server.test.model.po.Po;
import com.google.common.collect.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * @author xihao
 * @version 1.0
 * @since 06/03/2019
 */
public class CommonCreateServiceImpl<PARAM, PO extends Po, ID> implements CreateService<PARAM> {

    private ServiceMapper<PARAM, PO> serviceMapper;
    private JpaRepository<PO, ID> jpaRepository;

    public CommonCreateServiceImpl(ServiceMapper<PARAM, PO> serviceMapper, JpaRepository<PO, ID> repository) {
        this.serviceMapper = serviceMapper;
        this.jpaRepository = repository;
    }

    @Override
    @Transactional
    public Optional<Long> create(PARAM param) {
        Optional<PO> toCreate = serviceMapper.convert(param);
        Optional<PO> created = toCreate.map(po -> jpaRepository.save(po));
        // todo: think about info to display after creating
        return created.map(Po::getId);
    }

    @Override
    @Transactional
    public List<Long> create(List<PARAM> params) {
        return isEmpty(params) ?
                Lists.newArrayList() :
                params.stream()
                        .map(this::create)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList());
    }

}
