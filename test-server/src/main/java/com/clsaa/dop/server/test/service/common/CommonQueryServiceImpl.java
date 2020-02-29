package com.clsaa.dop.server.test.service.common;

import com.clsaa.dop.server.test.mapper.ServiceMapper;
import com.clsaa.rest.result.Pagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author xihao
 * @version 1.0
 * @since 12/03/2019
 */
public class CommonQueryServiceImpl<PO,DTO,ID> implements QueryService<DTO, ID> {

    private ServiceMapper<PO, DTO> serviceMapper;
    private JpaRepository<PO, ID> jpaRepository;

    public CommonQueryServiceImpl(ServiceMapper<PO, DTO> serviceMapper, JpaRepository<PO, ID> jpaRepository) {
        this.serviceMapper = serviceMapper;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<DTO> selectByIds(ID id) {
        Optional<PO> poOptional = jpaRepository.findById(id);
        return poOptional.isPresent() ? serviceMapper.convert(poOptional.get()) : Optional.empty();
    }

    @Override
    public List<DTO> selectByIds(List<ID> id) {
        List<PO> pos = jpaRepository.findAllById(id);
        return serviceMapper.convert(pos);
    }

    @Override
    public List<DTO> select() {
        List<PO> pos = jpaRepository.findAll();
        return serviceMapper.convert(pos);
    }

    @Override
    public Pagination<DTO> selectByPage(int pageNo, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "ctime");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<PO> pos = jpaRepository.findAll(pageable);
        return toPagination(pageNo, pageSize).apply(pos);
    }

    @Override
    public Long count() {
        return jpaRepository.count();
    }

    protected Function<Page<PO>, Pagination<DTO>> toPagination(int pageNo, int pageSize) {
        return pos -> {
            List<DTO> result = pos.get().map(serviceMapper::convert).map(Optional::get).collect(Collectors.toList());
            Pagination<DTO> pagination = new Pagination<>();
            pagination.setPageNo(pageNo);
            pagination.setPageSize(pageSize);
            pagination.setTotalCount((int) pos.getTotalElements());
            pagination.setPageList(result);
            return pagination;
        };
    }
}
