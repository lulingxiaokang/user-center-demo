package com.llxk.usercenterdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llxk.usercenterdemo.common.ErrorCode;
import com.llxk.usercenterdemo.exception.BusinessException;
import com.llxk.usercenterdemo.mapper.UserMapper;
import com.llxk.usercenterdemo.model.domain.User;
import com.llxk.usercenterdemo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.llxk.usercenterdemo.constant.UserConstant.*;



/**
* @author llxk
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-08-16 16:06:04
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        //1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if(userAccount.length() < MIN_USER_ACCOUNT_LENGTH){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过短");
        }
        if(userPassword.length() < MIN_USER_PASSWORD_LENGTH || checkPassword.length() < MIN_USER_PASSWORD_LENGTH){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码长度过短");
        }
        if(planetCode.length() > 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编号过长");
        }

        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }

        //密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码和校验密码相同");
        }

        //账户不能重复
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        int count = userMapper.selectCount(lambdaQueryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户重复");
        }

        //编号不能重复
        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getPlanetCode, planetCode);
        count = userMapper.selectCount(lambdaQueryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编号重复");
        }

        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean saveResult = this.save(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SAVE_FAILED, "注册失败");
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "有参数为空");
        }
        if(userAccount.length() < MIN_USER_ACCOUNT_LENGTH){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名过短");
        }
        if(userPassword.length() < MIN_USER_PASSWORD_LENGTH){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码长度过短");
        }

        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账户不能包含特殊字符");
        }

        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        //查询用户是否存在
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword);
        User user = userMapper.selectOne(lambdaQueryWrapper);
        //用户不存在
        if(user == null){
            log.info("user login failed, userAccount cannot match userPassword");
           throw new BusinessException(ErrorCode.NULL_ERROR, "用户不存在");
        }

        //3. 用户脱敏
        User safeUser = getSafetyUser(user);

        //4. 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safeUser);
        return safeUser;
    }

    /**
     * 用户脱敏
     * @param originUser 原用户
     * @return 脱敏用户
     */
    @Override
    public User getSafetyUser(User originUser){
        if (originUser == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User safeUser = new User();
        safeUser.setId(originUser.getId());
        safeUser.setUsername(originUser.getUsername());
        safeUser.setUserAccount(originUser.getUserAccount());
        safeUser.setAvatarUrl(originUser.getAvatarUrl());
        safeUser.setGender(originUser.getGender());
        safeUser.setPhone(originUser.getPhone());
        safeUser.setEmail(originUser.getEmail());
        safeUser.setUserRole(originUser.getUserRole());
        safeUser.setPlanetCode(originUser.getPlanetCode());
        safeUser.setUserStatus(originUser.getUserStatus());
        safeUser.setCreateTime(originUser.getCreateTime());
        return safeUser;
    }

    /**
     * 用户注销
     *
     * @param request
     * @return 注销结果
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        //移除用户登陆态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




