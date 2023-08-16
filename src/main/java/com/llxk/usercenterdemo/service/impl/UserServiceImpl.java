package com.llxk.usercenterdemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.llxk.usercenterdemo.mapper.UserMapper;
import com.llxk.usercenterdemo.model.domain.User;
import com.llxk.usercenterdemo.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.llxk.usercenterdemo.utils.SystemConstants.*;

/**
* @author llxk
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2023-08-16 16:06:04
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1. 校验
        if(StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)){
            return -1;
        }
        if(userAccount.length() < MIN_USER_ACCOUNT_LENGTH){
            return -1;
        }
        if(userPassword.length() < MIN_USER_PASSWORD_LENGTH || checkPassword.length() < MIN_USER_PASSWORD_LENGTH){
            return -1;
        }

        //账户不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            return -1;
        }

        //密码和校验密码相同
        if(!userPassword.equals(checkPassword)){
            return -1;
        }

        //账户不能重复
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        int count = userMapper.selectCount(lambdaQueryWrapper);
        if(count > 0){
            return -1;
        }
        //2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //3. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(userPassword);
        boolean saveResult = this.save(user);
        if(!saveResult){
            return -1;
        }

        return user.getId();
    }
}




