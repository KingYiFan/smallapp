package cn.cnbuilder.smallapp.mapper;

import cn.cnbuilder.smallapp.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<Map> selectAllUser();
}
