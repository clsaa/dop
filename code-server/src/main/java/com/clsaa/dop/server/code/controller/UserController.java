package com.clsaa.dop.server.code.controller;

import com.clsaa.dop.server.code.feign.UserCredentialType;
import com.clsaa.dop.server.code.feign.UserCredentialV1;
import com.clsaa.dop.server.code.feign.UserFeign;
import com.clsaa.dop.server.code.model.bo.user.SSHKeyBo;
import com.clsaa.dop.server.code.model.dto.user.SSHKeyDto;
import com.clsaa.dop.server.code.model.dto.user.UserDto;
import com.clsaa.dop.server.code.model.vo.user.SSHKeyVo;
import com.clsaa.dop.server.code.service.UserService;
import com.clsaa.dop.server.code.util.BeanUtils;
import com.clsaa.dop.server.code.util.RequestUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsy
 */
@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFeign userFeign;



//    @GetMapping("/users/publickey")
//    public String hh1(){
//        return userFeign.getAccountRSAPublicKey();
//    }
//
//
//    @PutMapping("/users")
//    public void hh2(@RequestParam String username,@RequestParam String password){
//        userService.updateUserPassword(username,password);
//    }


//    @ApiOperation(value = "新增一个用户",notes = "用用户名、密码和邮箱新建一个gitlab用户，并且创建access_token插入数据库")
//    @PostMapping("/users")
//    public void addUser(@ApiParam(value = "用户信息") @RequestBody UserDto userDto){
//        userService.addUser(userDto.getName(),userDto.getPassword(),userDto.getEmail());
//    }

//    @PostMapping("/users/{userId}/credential")
//    public void hh3(@PathVariable("userId") Long userId,
//                    @RequestParam("identifier") String identifier,
//                    @RequestParam("credential") String credential,
//                    @RequestParam("type") UserCredentialType type){
//
//        userFeign.addUserCredential(userId,identifier,credential,type);
//
//    }
//
//    @GetMapping("/users/{userId}/credential")
//    public UserCredentialV1 getUserCredentialV1ByUserId(@PathVariable("userId") Long userId, @RequestParam("type") UserCredentialType type){
//
//        return userFeign.getUserCredentialV1ByUserId(userId,type);
//    }

//    @PostMapping("/users")
//    public void addUser(@RequestParam("id") Long id,@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("email") String email){
//
//        userService.addUser(id,username,password,email);
//
//    }


    @GetMapping("/user/keys")
    @ApiOperation(value = "查询用户sshkey列表",notes = "根据用户id进行查询")
    public List<SSHKeyVo> findSSHKeys(@ApiParam(value = "用户id") @RequestHeader("x-login-user") Long userId){

        List<SSHKeyBo> sshKeyBos=userService.findSSHKeys(userId);
        List<SSHKeyVo> sshKeyVos=new ArrayList<>();
        for(SSHKeyBo sshKeyBo:sshKeyBos){
            sshKeyVos.add(BeanUtils.convertType(sshKeyBo,SSHKeyVo.class));
        }
        return sshKeyVos;

    }

    @PostMapping("/user/keys")
    @ApiOperation(value = "添加一个ssh key",notes = "根据用户的id添加一个ssh key，包括key和title")
    public void addSSHKey(@ApiParam(value = "ssh key信息")@RequestBody SSHKeyDto sshKeyDto){
        userService.addSSHKey(sshKeyDto.getKey(), sshKeyDto.getTitle(), sshKeyDto.getUserId());
    }


    @DeleteMapping("/user/keys/{id}")
    @ApiOperation(value = "删除一个ssh key",notes = "根据ssh key的id和userId删除ssh key")
    public void deleteSSHKey(@ApiParam(value = "ssh key的id")@PathVariable("id") int id,
                             @ApiParam(value = "用户id")@RequestHeader("x-login-user") Long userId){
        userService.deleteSSHkey(id,userId);
    }



}
