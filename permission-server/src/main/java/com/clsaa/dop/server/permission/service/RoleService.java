package com.clsaa.dop.server.permission.service;

import com.clsaa.dop.server.permission.annotation.GetUserId;
import com.clsaa.dop.server.permission.annotation.PermissionName;
import com.clsaa.dop.server.permission.config.BizCodes;
import com.clsaa.dop.server.permission.dao.RoleRepository;
import com.clsaa.dop.server.permission.model.bo.PermissionBoV1;
import com.clsaa.dop.server.permission.model.bo.RoleBoV1;
import com.clsaa.dop.server.permission.model.po.Role;
import com.clsaa.dop.server.permission.model.vo.RoleV1;
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
 *  角色的增删改查
 *
 * @author lzy
 *
 * @since 2019.3.9
 */

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    //功能点service
    private PermissionService permissionService;

    @Autowired
    //关联关系service
    private RolePermissionMappingService rolePermissionMappingService;

    @Autowired
    //关联关系service
    private UserRoleMappingService userRoleMappingService;

    @Autowired
    //数据规则
    private UserRuleService userRuleService;

    @Autowired
    //权限管理服务
    private AuthenticationService authenticationService;

    @Autowired
    //用户服务
    private UserFeignService userFeignService;

    /* *
     *
     *  * @param id 角色ID
     *  * @param parentId 父角色ID
     *  * @param name 角色名称
     *
     *  * @param ctime 创建时间
     *  * @param mtime 修改时间
     *  * @param cuser 创建人
     *  * @param muser 修改人
     *  * @param deleted 删除标记
     *
     * since :2019.3.1
     */

    //创建一个角色
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    @PermissionName(name = "创建角色")
    public Long createRole(@GetUserId Long cuser, Long parentId, String name, Long muser)
    {
            Role existRole=this.roleRepository.findByName(name);
            BizAssert.allowed(existRole==null, BizCodes.REPETITIVE_ROLE_NAME);
            Role role= Role.builder()
                    .parentId(parentId)
                    .name(name)
                    .cuser(cuser)
                    .muser(muser)
                    .ctime(LocalDateTime.now())
                    .mtime(LocalDateTime.now())
                    .deleted(false)
                    .build();
            roleRepository.saveAndFlush(role);
            authenticationService.addData(
                    authenticationService.findUniqueRule("in","roleId",
                            authenticationService.findByName("权限管理员").getId()).getId(),
                    cuser,role.getId(),cuser);
            authenticationService.addData(
                    authenticationService.findUniqueRule("equals","roleId",
                            authenticationService.findByName("权限管理员").getId()).getId(),
                    cuser,role.getId(),cuser);
            return role.getId();

    }

    //根据ID查询角色
    public Role findById(Long id)
    {
        Optional<Role> role=roleRepository.findById(id);
        if(role.isPresent())
        {
            return role.get();
        }
        return null;
    }
    //根据name查询角色
    public RoleBoV1 findByname(String name)
    {
        return BeanUtils.convertType(this.roleRepository.findByName(name), RoleBoV1.class);
    }
    //分页查询所有角色

    @PermissionName(name = "查询角色")
    public Pagination<RoleV1> getRoleV1Pagination(@GetUserId Long userId,Integer pageNo, Integer pageSize,String key)
    {
        Sort sort = new Sort(Sort.Direction.DESC, "mtime");

        Pagination<RoleV1> pagination = new Pagination<>();
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);


        //可以查看的ID列表
        List<Long> idList=authenticationService.findAllIds("查询角色",userId,"roleId");

        List<Role> roleList=new ArrayList<>();
        if(key.equals(""))
        {
             roleList=this.roleRepository.findByIdIn(idList,pagination.getRowOffset(),pagination.getPageSize());
        }
        else
        {
             roleList = this.roleRepository.findAllByNameLikeAndIdIn(key,idList,pagination.getRowOffset(),
                     pagination.getPageSize());
        }
        int count=roleList.size();
        pagination.setTotalCount(count);
        if (count == 0) {
            pagination.setPageList(Collections.emptyList());
            return pagination;
        }

        //类型转换
        List<RoleV1> roleV1List=roleList.stream().map(p -> BeanUtils.convertType(p, RoleV1.class)).collect(Collectors.toList());


        //获取每条数据的创建人
        Map<Long,String> userMap=new HashMap<>();
        for(RoleV1 roleV1 : roleV1List)
        {
            if(!userMap.containsKey(roleV1.getMuser()))
            {
                userMap.put(
                        roleV1.getMuser(),
                        userFeignService.findUserByIdV1(roleV1.getMuser()).getName());
            }
        }
        for(RoleV1 roleV1 : roleV1List)
        {
            roleV1.setUserName(userMap.get(roleV1.getMuser()));
        }

        pagination.setPageList(roleV1List);
        return pagination;
    }
    //根据name查询角色
    public RoleBoV1 findByName(String name)
    {
        return BeanUtils.convertType(this.roleRepository.findByName(name), RoleBoV1.class);
    }
    //根据ID删除角色,并删除关联关系和数据规则

    @PermissionName(name="删除角色")
    public void deleteById(@GetUserId Long userId,Long id)
    {
            if(authenticationService.check("删除角色",userId,"roleId",id))
            {
                rolePermissionMappingService.deleteByRoleId(id);
                userRuleService.deleteByRoleId(id);
                roleRepository.deleteById(id);
            }
    }

    //查询所有权限的ID和名称
    public List<PermissionBoV1> findAllPermission(Long loginUser)
    {
        return permissionService.findAll(loginUser);
    }

    //根据功能点ID查询角色
    public List<RoleBoV1> findByPermissionId(Long permissionId)
    {
        List<RoleBoV1> roleBoV1List=roleRepository.findByPermissionId(permissionId)
                .stream().map(p->BeanUtils.convertType(p, RoleBoV1.class)).collect(Collectors.toList());
        return roleBoV1List;
    }

    //根据用户ID查询角色
    public List<RoleBoV1> findByUserId(Long userId)
    {
        List<RoleBoV1> roleBoV1List =roleRepository.findByUserId(userId)
                .stream().map(p->BeanUtils.convertType(p, RoleBoV1.class)).collect(Collectors.toList());
        return roleBoV1List;
    }

    //查询所有角色，为用户绑定时使用
    @PermissionName(name = "查询角色")
    public List<RoleBoV1> findAllRole(@GetUserId Long userId)
    {
        //可以查看的ID列表
        List<Long> idList=authenticationService.findAllIds("查询角色",userId,"roleId");
        return roleRepository.findByIdIn(idList).stream().map(p ->
                BeanUtils.convertType(p, RoleBoV1.class)).collect(Collectors.toList());
    }

}
