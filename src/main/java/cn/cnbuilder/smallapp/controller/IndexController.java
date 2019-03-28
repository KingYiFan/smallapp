package cn.cnbuilder.smallapp.controller;

import cn.cnbuilder.smallapp.controller.base.BaseController;
import cn.cnbuilder.smallapp.service.IUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(description = "首页接口")
@Controller
public class IndexController extends BaseController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "首页", notes = "首页")
    @GetMapping(value = "/")
    @ResponseBody
    public Object index() {
        List<Map> users = userService.selectAllUser();
        for (Map user : users) {
            System.out.println(user);
        }
        return "Hello,Friend。This is KingYiFan 's smallApp demo！";

    }

}

