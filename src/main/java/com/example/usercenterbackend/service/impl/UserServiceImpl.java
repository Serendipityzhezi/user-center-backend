package com.example.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.service.UserService;
import com.example.usercenterbackend.modal.domain.User;
import com.example.usercenterbackend.Mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 13425
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-03-06 19:21:19
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.账户，密码，校验码为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        // 2.账户小于4位
        if (userAccount.length() < 4) {
            return null;
        }
        // 3.密码，校验码小于8位
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return null;
        }
        // 4.账户包含特殊字符（正则表达式）
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }
        // 5.密码和校验码不同
        if (!userPassword.equals(checkPassword)) {
            return null;
        }
        // 6.账户重复,放在后面，可以节省查询次数，节省内存性能
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        Long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return null;
        }
        //校验完成后，加密
        String SALT = "yupi";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        //注册成功，插入数据到数据库
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean result = this.save(user);
        if (!result) {
            return null;
        }
        return user.getId();
    }
}



