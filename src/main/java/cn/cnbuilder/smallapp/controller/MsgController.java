package cn.cnbuilder.smallapp.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.*;
import cn.cnbuilder.smallapp.config.WxMaConfiguration;
import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信小程序推送接口
 * create By KingYiFan 2019/03/19
 */
@Api(description = "推送接口")
@RestController
@RequestMapping("/wx/msg/{appId}")
public class MsgController {

    @Value("${wxPush.orderTemplateId}")
    private String orderTemplateId;

    /**
     * 微信小程序推送
     * create By KingYiFan 2019/03/19
     */
    @ApiOperation(value = "微信推送", notes = "微信推送")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fromId", value = "推送id", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "appId", value = "小程序appId", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "openId", value = "用户openId", dataType = "String", paramType = "query")})
    @GetMapping(value = "/sendTemplateMessage")
    @ResponseBody
    public Object sendTemplateMessage(@PathVariable String appId, String fromId, String openId) throws Exception {

        WxMaTemplateMessage wxMaTemplateMessage = new WxMaTemplateMessage();

        List<WxMaTemplateData> wxMaTemplateData = new ArrayList<>();
        WxMaTemplateData wxMaTemplateData1 = new WxMaTemplateData();
        wxMaTemplateData1.setName("keyword1");
        wxMaTemplateData1.setValue("多多支持KingYiFan");
        wxMaTemplateData.add(wxMaTemplateData1);

        WxMaTemplateData wxMaTemplateData2 = new WxMaTemplateData();
        wxMaTemplateData2.setName("keyword2");
        wxMaTemplateData2.setValue("www.cnbuilder.cn");
        wxMaTemplateData.add(wxMaTemplateData2);

        WxMaTemplateData wxMaTemplateData4 = new WxMaTemplateData();
        wxMaTemplateData4.setName("keyword3");
        wxMaTemplateData4.setValue(DateUtil.now());
        wxMaTemplateData.add(wxMaTemplateData4);

        WxMaTemplateData wxMaTemplateData5 = new WxMaTemplateData();
        wxMaTemplateData5.setName("keyword4");
        wxMaTemplateData5.setValue("喜欢我，就请我喝一瓶哇哈哈哈！");
        wxMaTemplateData.add(wxMaTemplateData5);
        wxMaTemplateMessage.setPage("pages/index/index");

        wxMaTemplateMessage.setData(wxMaTemplateData);
        wxMaTemplateMessage.setEmphasisKeyword("keyword2.DATA");
        wxMaTemplateMessage.setTemplateId(orderTemplateId);
        wxMaTemplateMessage.setFormId(fromId);
        wxMaTemplateMessage.setToUser(openId);
        final WxMaService wxService = WxMaConfiguration.getMaService(appId);

        try {
            wxService.getMsgService().sendTemplateMsg(wxMaTemplateMessage);
            return "推送成功";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "推送失败";
    }
}