package com.seven.wqb.dao;

import com.seven.wqb.domain.RefreshTokenDetail;
import com.seven.wqb.domain.User;
import com.seven.wqb.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserDao {

    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);

    User getUserById(Long userId);

    UserInfo getUserInfoByUserId(Long userId);

    Integer updateUsers(User user);

    Integer updateUserInfos(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(Set<Long> userIdSet);

    Integer pageCountUserInfos(Map<String, Object> param);

    List<UserInfo> pageListUserInfos(Map<String, Object> param);

    Integer deleteRefreshToken(@Param("refreshToken") String refreshToken,
                               @Param("userId") Long userId);

    Integer addRefreshToken(@Param("refreshToken") String refreshToken,
                            @Param("userId") Long userId,
                            @Param("createTime") Date date);

    RefreshTokenDetail getRefreshTokenDetail(String refreshToken);

    RefreshTokenDetail getRefreshTokenByUserId(Long userId);
}
