package com.llxk.usercenterdemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.llxk.usercenterdemo.common.BaseResponse;
import com.llxk.usercenterdemo.common.ErrorCode;
import com.llxk.usercenterdemo.exception.BusinessException;
import com.llxk.usercenterdemo.model.domain.User;
import com.llxk.usercenterdemo.model.domain.request.UserLoginRequest;
import com.llxk.usercenterdemo.model.domain.request.UserRegisterRequest;
import com.llxk.usercenterdemo.service.UserService;
import com.llxk.usercenterdemo.utils.ResultUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "有参数为空");
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(result);

    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "有参数为空");
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request){
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }


    @GetMapping("/current")
    public BaseResponse<User> getCurrent(HttpServletRequest request){
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(currentUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        //TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }



    /**
     * 查询用户
     * @param username 查询条件
     * @return 查询到的用户
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request){
        //仅管理员可查询
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "无管理员权限");
        }
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            lambdaQueryWrapper.like(User::getUsername, username);
        }
        List<User> userList = userService.list(lambdaQueryWrapper);
        userList = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(userList);

    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request){
        //仅管理员可删除
        if(!isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH, "无管理员权限");
        }
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id不合法");
        }
        boolean result = userService.removeById(id);
        return ResultUtils.success(result);
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
