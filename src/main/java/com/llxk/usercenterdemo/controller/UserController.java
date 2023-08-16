package com.llxk.usercenterdemo.controller;

import com.llxk.usercenterdemo.model.domain.User;
import com.llxk.usercenterdemo.model.domain.request.UserLoginRequest;
import com.llxk.usercenterdemo.model.domain.request.UserRegisterRequest;
import com.llxk.usercenterdemo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ClassName: UserController
 * Package: com.llxk.usercenterdemo.controller
 *
 * @author 庐陵小康
 * @version 1.0
 * @Desc 用户接口
 * @Date 2023/8/16 20:35
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;


    @PostMapping("/register")
    public Long userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            return null;
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return null;
        }
        long id = userService.userRegister(userAccount, userPassword, checkPassword);
        return id;
    }

    @PostMapping("/login")
    public User userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            return null;
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return user;
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username){


    }


}
