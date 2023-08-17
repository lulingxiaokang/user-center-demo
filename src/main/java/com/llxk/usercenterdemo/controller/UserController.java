package com.llxk.usercenterdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.llxk.usercenterdemo.model.domain.User;
import com.llxk.usercenterdemo.model.domain.request.UserLoginRequest;
import com.llxk.usercenterdemo.model.domain.request.UserRegisterRequest;
import com.llxk.usercenterdemo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.llxk.usercenterdemo.constant.UserConstant.ADMIN_ROLE;
import static com.llxk.usercenterdemo.constant.UserConstant.USER_LOGIN_STATE;


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

    @PostMapping("/logout")
    public Integer userLogout(HttpServletRequest request){
        if (request == null) {
            return null;
        }
        return userService.userLogout(request);
    }


    @GetMapping("/current")
    public User getCurrent(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            return null;
        }
        long userId = currentUser.getId();
        //TODO 校验用户是否合法
        User user = userService.getById(userId);
        return userService.getSafetyUser(user);
    }



    /**
     * 查询用户
     * @param username 查询条件
     * @return 查询到的用户
     */
    @GetMapping("/search")
    public List<User> searchUsers(String username, HttpServletRequest request){
        //仅管理员可查询
        if(!isAdmin(request)){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            lambdaQueryWrapper.like(User::getUsername, username);
        }
        List<User> userList = userService.list(lambdaQueryWrapper);
        return userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());

    }

    @PostMapping("/delete")
    public boolean deleteUser(@RequestBody long id, HttpServletRequest request){
        //仅管理员可删除
        if(!isAdmin(request)){
            return false;
        }
        if(id <= 0){
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 是否为管理员
     * @param request request
     * @return 是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }


}
