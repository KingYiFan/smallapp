> 本项目用SpringBoot构建的小程序demo程序0.01版本
①：主要功能就是小程序后台开发基本功能
    小程序推送代码：
    ```
    <form   report-submit='true' bindsubmit='getFormID'>
          <button form-type="submit" class="zan-btn zan-btn--large zan-btn--danger payButton">点我会生成fromid~~~</button>
    </form>
    //js
    //获取应用实例
    const app = getApp()
    Page({
      getFormID: function (e) {
        console.log(e.detail.formId)
      }
    })
     ```

②：集成了微信小程序推送
③：常用的工具类
④：腾讯地图webService开发
。。。

