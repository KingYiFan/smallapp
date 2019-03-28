package cn.cnbuilder.smallapp.service.impl;

import cn.cnbuilder.smallapp.entity.User;
import cn.cnbuilder.smallapp.mapper.UserMapper;
import cn.cnbuilder.smallapp.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


    @Autowired
    private UserMapper userMapper;


    @Override
    public List<Map> selectAllUser() {
        return userMapper.selectAllUser();
    }
}

