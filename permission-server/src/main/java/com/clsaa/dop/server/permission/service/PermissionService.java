package com.clsaa.dop.server.permission.service;


import com.clsaa.dop.server.permission.annotation.GetUserId;
import com.clsaa.dop.server.permission.annotation.PermissionName;
import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.PermissionRepository;
import com.clsaa.dop.server.permission.model.bo.PermissionBoV1;
import com.clsaa.dop.server.permission.model.po.Permission;
import com.clsaa.dop.server.permission.model.vo.PermissionV1;
import com.clsaa.dop.server.permission.util.BeanUtils;
import com.clsaa.rest.result.Pagination;
import com.clsaa.rest.result.bizassert.BizAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
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
    @Autowired
    private RoleService roleService;

    @Autowired
    //关联关系service
    private RolePermissionMappingService rolePermissionMappingService;
    @Autowired
    //关联关系service
    private UserRoleMappingService userRoleMappingService;

    @Autowired
    //权限管理服务
    private AuthenticationService authenticationService;

    @Autowired
    //用户服务
    private UserFeignService userFeignService;

/* *
 *
 *  * @param id 功能点ID
 *  * @param parentId 父功能点ID
 *  * @param name 功能点名称
 *  * @param isPrivate 是否私有
 *  * @param description 功能点描述
 *  * @param ctime 创建时间
 *  * @param mtime 修改时间
 *  * @param cuser 创建人
 *  * @param muser 修改人
 *  * @param deleted 删除标记
 *
 * since :2019.3.1
 */


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    //创建一个功能点
    @PermissionName(name = "创建功能点")

    public void createPermission(@GetUserId Long cuser, Long parentId, String name, Integer isPrivate, String description, Long muser)
    {
            Permission existPermission=this.permissionRepository.findByName(name);
            BizAssert.allowed(existPermission==null, BizCodes.REPETITIVE_PERMISSION_NAME);
            Permission  permission= Permission.builder()
                    .parentId(parentId)
                    .name(name)
                    .isPrivate(isPrivate)
                    .description(description)
                    .cuser(cuser)
                    .muser(muser)
                    .ctime(LocalDateTime.now())
                    .mtime(LocalDateTime.now())
                    .deleted(false)
                    .build();
            permissionRepository.saveAndFlush( permission);
            authenticationService.addData(
                    authenticationService.findUniqueRule("in","permissionId",
                            authenticationService.findByName("权限管理员").getId()).getId(),
                    cuser,permission.getId(),cuser);
            authenticationService.addData(
                    authenticationService.findUniqueRule("equals","permissionId",
                            authenticationService.findByName("权限管理员").getId()).getId(),
                    cuser,permission.getId(),cuser);
    }

    //根据ID查询功能点
    public Permission findById(Long id)
    {
        Optional<Permission> permission=permissionRepository.findById(id);
        if(permission.isPresent())
        {
            return permission.get();
        }
        return null;
    }
    //分页查询所有功能点带数据权限

    @PermissionName(name = "查询功能点")
    public Pagination<PermissionV1> getPermissionV1Pagination(@GetUserId Long userId, Integer pageNo, Integer pageSize, String key)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "mtime");

        Pagination<PermissionV1> pagination = new Pagination<>();
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);

//        可以查看的ID列表
        List<Long> idList=authenticationService.findAllIds("查询功能点",userId,"permissionId");

        List<Permission> permissionList=new ArrayList<>();
        if(key.equals(""))
        {
            permissionList=this.permissionRepository.findByIdIn(idList,pagination.getRowOffset(),pagination.getPageSize());
        }
        else
        {
            permissionList = this.permissionRepository.findAllByNameLikeAndIdIn(key,idList,pagination.getRowOffset(),
                    pagination.getPageSize());
        }

        int count=permissionList.size();
        pagination.setTotalCount(count);
        if (count== 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }

        //类型转换
        List<PermissionV1> permissionV1List=permissionList.stream().
                map(p -> BeanUtils.convertType(p, PermissionV1.class)).collect(Collectors.toList());

        //获取每条数据的创建人
        Map<Long,String> userMap=new HashMap<>();
        for(PermissionV1 permissionV1 : permissionV1List)
        {
            if(!userMap.containsKey(permissionV1.getCuser()))
            {
                userMap.put(
                        permissionV1.getCuser(),
                        userFeignService.findUserByIdV1(permissionV1.getCuser()).getName());
            }
        }
        for(PermissionV1 permissionV1 : permissionV1List)
        {
            permissionV1.setUserName(userMap.get(permissionV1.getCuser()));
        }
        pagination.setPageList(permissionV1List);
        return pagination;
    }

    //根据name查询功能点
    public PermissionBoV1 findByName(String name)
    {

       return BeanUtils.convertType(this.permissionRepository.findByName(name), PermissionBoV1.class);
    }

    //根据ID删除功能点,并删除关联关系
    @Transactional
    @PermissionName(name = "删除功能点")
    public void deleteById(@GetUserId Long userId,Long id)
    {
            if(authenticationService.check("删除功能点",userId,"permissionId",id))
            {
                rolePermissionMappingService.deleteByPermissionId(id);
                permissionRepository.deleteById(id);
            }
    }

    //创建或编辑角色时，需要勾选该角色对应的功能点，所以要返回全部功能点
    @PermissionName(name = "查询功能点")
    public List<PermissionBoV1> findAll(@GetUserId Long userId)
    {
        //可以查看的ID列表
        List<Long> idList=authenticationService.findAllIds("查询功能点",userId,"permissionId");
        return permissionRepository.findByIdIn(idList).stream().map(p ->
                BeanUtils.convertType(p, PermissionBoV1.class)).collect(Collectors.toList());
    }

    //根据角色ID查询功能点
    public List<PermissionBoV1> findByRoleId(Long roleId)
    {

        return permissionRepository.findByRoleId(roleId).stream().map(p->
                BeanUtils.convertType(p, PermissionBoV1.class)).collect(Collectors.toList());

    }

    //根据用户ID查询功能点
    public List<PermissionBoV1> findByUserId(Long userId)
    {
        List<PermissionBoV1>permissionBoV1List=permissionRepository.findByUserId(userId).stream().map(p->
                BeanUtils.convertType(p, PermissionBoV1.class)).collect(Collectors.toList());
        //去重
        for  ( int  i  =   0 ; i  <  permissionBoV1List.size()  -   1 ; i ++ )  {
            for  ( int  j  =  permissionBoV1List.size()  -   1 ; j  >  i; j -- )  {
                if  (permissionBoV1List.get(j).getId().equals(permissionBoV1List.get(i).getId()))  {
                    permissionBoV1List.remove(j);
                }
            }
        }
        return permissionBoV1List;

    }

    //判断用户是否拥有特定功能点
    public boolean checkUserPermission(String permissionName,Long userId)
    {
       if (permissionRepository.findByUserIdAndPermissionName(userId,permissionName).isEmpty())
       {return false;}
        return true;

    }
}
