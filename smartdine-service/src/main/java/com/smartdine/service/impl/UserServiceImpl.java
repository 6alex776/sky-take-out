package com.smartdine.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.smartdine.constant.MessageConstant;
import com.smartdine.dto.UserLoginDTO;
import com.smartdine.entity.User;
import com.smartdine.mapper.UserMapper;
import com.smartdine.properties.WeChatProperties;
import com.smartdine.service.UserService;
import com.smartdine.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) throws Exception {

        //调用微信登录接口，获取openid
        Map<String, String> map = new HashMap<>();
        map.put("appid", weChatProperties.getAppid());
        map.put("secret", weChatProperties.getSecret());
        map.put("js_code", userLoginDTO.getCode());
        map.put("grant_type", "authorization_code");

        String result = HttpClientUtil.doGet(WX_LOGIN, map);// 必须用doGet

        JSONObject jsonObject = JSONObject.parseObject(result);
        String openid = jsonObject.getString("openid");

// 新增错误码判断，便于排查
        if (jsonObject.containsKey("errcode")) {
            log.error("微信登录接口错误：{}，错误信息：{}",
                    jsonObject.getString("errcode"),
                    jsonObject.getString("errmsg"));
            throw new Exception(MessageConstant.LOGIN_FAILED);
        }

        if (openid == null || openid.isEmpty()) {
            throw new Exception(MessageConstant.LOGIN_FAILED);
        }


        User user = userMapper.getByOpenid(openid);

        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
        }
        return user;
    }
}
