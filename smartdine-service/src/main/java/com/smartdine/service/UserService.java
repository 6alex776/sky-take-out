package com.smartdine.service;

import com.smartdine.dto.UserLoginDTO;
import com.smartdine.entity.User;

public interface UserService {

    User wxLogin(UserLoginDTO userLoginDTO) throws Exception;
}
