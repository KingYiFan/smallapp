package cn.cnbuilder.smallapp.service;

import cn.cnbuilder.smallapp.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;


public interface IUserService extends IService<User> {


    List<Map> selectAllUser();
}

