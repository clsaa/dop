package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.dao.PermissionRepository;
import com.clsaa.dop.server.permission.model.bo.PermissionBoV1;
import com.clsaa.dop.server.permission.model.po.Permission;
import com.clsaa.dop.server.permission.model.vo.PermissionV1;
import com.clsaa.dop.server.permission.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 *  功能点的增删改查
 *
 * @author lzy
 *
 */
@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;
/* *
 *
 * @param id            功能点ID
 * @param parentId     父功能点ID
 * @param name     功能点名称
 * @param isPrivate         是否私有
 * @param description     功能点描述
 * @param ctime     创建时间
 * @param mtime     修改时间
 * @param cuser     创建人
 * @param muser     修改人
 * @param deleted     删除标记
 *
 * since :2019.3.1
 */


    //创建一个功能点
    public void createPermission(Long parentId,String name,Integer isPrivate,String description,
                                       Long cuser,Long muser,Boolean deleted)
    {
        Permission permission= Permission.builder().
                parentId(parentId)
                .name(name)
                .isPrivate(isPrivate)
                .description(description)
                .cuser(cuser)
                .muser(muser)
                .ctime(LocalDateTime.now())
                .mtime(LocalDateTime.now())
                .deleted(deleted)
                .build();
         permissionRepository.saveAndFlush(permission);

    }

    //根据ID查询功能点
    public Permission findById(Long id)
    {
        Optional<Permission> permission=permissionRepository.findById(id);
//        <PermissionBoV1> permissionBoV1=BeanUtils.convertType(permission, PermissionBoV1.class);
        if(permission.isPresent())
        {
            return permission.get();
        }
        return null;
    }
    //分页查询所有功能点
    public Pagination<PermissionV1> getPermissionV1Pagination(Integer pageNo, Integer pageSize)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "mtime");
        int count = (int) this.permissionRepository.count();

        Pagination<PermissionV1> pagination = new Pagination<>();
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        pagination.setTotalCount(count);

        if (count == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }

        Pageable pageRequest = PageRequest.of(pagination.getPageNo() - 1, pagination.getPageSize(), sort);
        List<Permission> permissionList = this.permissionRepository.findAll(pageRequest).getContent();
        pagination.setPageList(permissionList.stream().map(p -> BeanUtils.convertType(p, PermissionV1.class)).collect(Collectors.toList()));

        return pagination;
    }

    //查询所有功能点
    public List<PermissionV1> findAll(){

        List<Permission> permissionList=this.permissionRepository.findAll();
        //将PO层对象list转换为BO层对象LIST
        return permissionList.stream().map(p -> BeanUtils.convertType(p, PermissionV1.class)).collect(Collectors.toList());

    }

    //根据ID删除功能点
    @Transactional
    public void deleteById(Long id)
    {
        permissionRepository.deleteById(id);
    }
    //删除所有功能点
    public void deleteAll()
    {
        permissionRepository.deleteAll();
    }
}