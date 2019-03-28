package cn.cnbuilder.smallapp.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;

import cn.cnbuilder.smallapp.config.WxMaConfiguration;
import cn.cnbuilder.smallapp.utils.JsonUtil;
import io.swagger.annotations.*;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信小程序用户接口
 * create By KingYiFan 2019/03/19
 */
@Api(description = "用户接口")
@RestController
@RequestMapping("/wx/user/{appId}")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 登陆接口
     */
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name="appId",value="小程序appId",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="code",value="用户code",dataType="String", paramType = "query")})
    @GetMapping("/login")
    public String login(
            @PathVariable  String appId,
             String code) {
        if (StringUtils.isBlank(code)) {
            return "code不能为空";
        }
        final WxMaService wxService = WxMaConfiguration.getMaService(appId);
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());
            //TODO 可以增加自己的逻辑，关联业务相关数据
            return JsonUtil.toJson(session);
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return e.toString();
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @ApiOperation(value = "获取用户信息", notes = "获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name="appId",value="小程序appId",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="sessionKey",value="登录接口返回的sessionKey",dataType="String", paramType = "query")
    })
    @GetMapping("/userInfo")
    public String info(
           @PathVariable  String appId,
           String sessionKey,
           String signature,
           String rawData,
           String encryptedData,
           String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appId);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return JsonUtil.toJson(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @ApiOperation(value = "获取用户手机号", notes = "获取用户手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name="appId",value="小程序appId",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="sessionKey",value="登录接口返回的sessionKey",dataType="String", paramType = "query")
    })
    @GetMapping("/getUserPhone")
    public String phone(
            @PathVariable String appId,
            String sessionKey,
            String signature,
            String rawData,
            String encryptedData,
            String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appId);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }
        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);
        return JsonUtil.toJson(phoneNoInfo);
    }

}