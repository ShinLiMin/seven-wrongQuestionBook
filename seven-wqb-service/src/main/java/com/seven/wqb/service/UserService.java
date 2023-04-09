package com.seven.wqb.service;

import com.mysql.cj.util.StringUtils;
import com.seven.wqb.dao.UserDao;
import com.seven.wqb.domain.RefreshTokenDetail;
import com.seven.wqb.domain.User;
import com.seven.wqb.domain.UserInfo;
import com.seven.wqb.domain.constant.UserConstant;
import com.seven.wqb.domain.exception.ConditionException;
import com.seven.wqb.service.utils.MD5Util;
import com.seven.wqb.service.utils.RSAUtil;
import com.seven.wqb.service.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public User getUserByPhone(String phone) {
        return userDao.getUserByPhone(phone);
    }

    public void addUser(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空");
        }
        User dbUser = getUserByPhone(phone);
        if (dbUser != null) {
            throw new ConditionException("手机号已注册");
        }
        Date now = new Date();
        String salt = String.valueOf(now.getTime());
        String password = user.getPassword();
        String rawPwd;
        try {
            rawPwd = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码加密错误");
        }
        String md5Pwd = MD5Util.sign(rawPwd, salt, "UTF-8");
        user.setCreateTime(now);
        user.setSalt(salt);
        user.setPassword(md5Pwd);
        Integer count = userDao.addUser(user);
        if (count < 1) {
            throw new ConditionException("注册失败");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setAvatar(UserConstant.DEFAULT_AVATAR);
        userInfo.setCreateTime(now);
        count = userDao.addUserInfo(userInfo);
        if (count < 1) {
            throw new ConditionException("信息保存失败");
        }
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ConditionException("用户不存在");
        }
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    public void updateUsers(User user) {
        Date updateTime = new Date();
        User dbUser = userDao.getUserById(user.getId());
        if (dbUser == null) {
            throw new ConditionException("用户不存在");
        }
        String password = user.getPassword();
        String rawPwd;
        try {
            rawPwd = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码加密错误");
        }
        String salt = user.getSalt();
        String md5Pwd = MD5Util.sign(rawPwd, salt, "UTF-8");
        user.setUpdateTime(updateTime);
        user.setPassword(md5Pwd);
        userDao.updateUsers(user);
    }

    public void updateUserInfos(UserInfo userInfo) {
        Date updateTime = new Date();
        User dbUser = userDao.getUserById(userInfo.getUserId());
        if (dbUser == null) {
            throw new ConditionException("用户不存在");
        }
        userInfo.setUpdateTime(updateTime);
        userDao.updateUserInfos(userInfo);
    }

    public Map<String, Object> loginForDts(User user) {
        String phone = user.getPhone();
        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空");
        }
        User dbUser = getUserByPhone(phone);
        if (dbUser == null) {
            throw new ConditionException("用户不存在");
        }
        String password = user.getPassword();
        String rawPwd;
        try {
            rawPwd = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码加密错误");
        }
        String salt = dbUser.getSalt();
        String md5Pwd = MD5Util.sign(rawPwd, salt, "UTF-8");
        if (!dbUser.getPassword().equals(md5Pwd)) {
            throw new ConditionException("密码错误");
        }
        Long userId = dbUser.getId();
        String accessToken;
        String refreshToken;
        try {
            accessToken = TokenUtils.generateToken(userId);
            refreshToken = TokenUtils.generateRefreshToken(userId);
        } catch (Exception e) {
            throw new ConditionException("token生成失败");
        }
        userDao.deleteRefreshToken(refreshToken, userId);
        userDao.addRefreshToken(refreshToken, userId, new Date());
        Map<String, Object> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        return result;
    }

    public void logout(String refreshToken, Long userId) {
        userDao.deleteRefreshToken(refreshToken,userId);
    }

    public String refreshAccessToken(String refreshToken) throws Exception{
        RefreshTokenDetail refreshTokenDetail = userDao.getRefreshTokenDetail(refreshToken);
        if (refreshTokenDetail == null) {
            throw new ConditionException("555","请重新登录");
        }
        TokenUtils.verifyToken(refreshToken);
        Long userId = refreshTokenDetail.getUserId();
        return TokenUtils.generateToken(userId);
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> userIdSet) {
        return userDao.getUserInfoByUserIds(userIdSet);
    }

    public String getRefreshTokenByUserId(Long userId) {
        RefreshTokenDetail refreshTokenDetail = userDao.getRefreshTokenByUserId(userId);
        return refreshTokenDetail.getRefreshToken();
    }
}
