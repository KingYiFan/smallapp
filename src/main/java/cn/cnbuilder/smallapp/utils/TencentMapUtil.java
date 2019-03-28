package cn.cnbuilder.smallapp.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 腾讯地图api
 *
 * @author KingYiFan
 */
@Component
public class TencentMapUtil {

    private static String key;

    @Value("${tencentMap.key}")
    public void setKey(String key) {
        TencentMapUtil.key = key;
    }

    /**
     * @Description: 通过经纬度获取位置
     * @Param: [lng, lat]
     * @Date: 2019/03/27
     */
    public static Map<String, Object> getLocation(String lng, String lat) {

        Map<String, Object> resultMap = new HashMap<>();

        // 参数解释：lng：经度，lat：维度。key：腾讯地图key
        String urlString = "https://apis.map.qq.com/ws/geocoder/v1/?location=" +
                lat + "," + lng + "&key=" + key;

        //根据url调用接口
        String result = getResult(urlString);

        // 转JSON格式
        JSONObject jsonObject = JSONObject.parseObject(result);
        String status = jsonObject.getString("status");
        if (!"0".equals(status)) {
            resultMap.put("status", jsonObject.getString("message"));
            return resultMap;
        }
        // 获取地址（行政区划信息） 包含有国籍，省份，城市
        JSONObject a = jsonObject.getJSONObject("result");
        //地址
        String address = a.getString("address");

        JSONObject address_reference = a.getJSONObject("address_reference");

        JSONObject landmark_l2 = address_reference.getJSONObject("landmark_l2");

        String shortName = landmark_l2.getString("title");

        resultMap.put("shortAddName", shortName);
        resultMap.put("longAddName", address);
        return resultMap;
    }

    /**
     * @Description: 通过位置获取经纬度
     * @return: Map
     * @Date: 2019/03/27
     */
    public static Map<String, Object> getlngAndlat(String address) {

        Map<String, Object> resultMap = new HashMap<>();

        // 参数解释：address 地址。key：腾讯地图key
        String urlString = "https://apis.map.qq.com/ws/geocoder/v1/?address=" +
                address + "&key=" + key;

        //范湖结果集
        String result = getResult(urlString);

        // 转JSON格式
        JSONObject jsonObject = JSONObject.parseObject(result);
        String status = jsonObject.getString("status");
        if (!"0".equals(status)) {
            resultMap.put("status", jsonObject.getString("message"));
            return resultMap;
        }
        //System.out.println(jsonObject);
        JSONObject a = jsonObject.getJSONObject("result");
        JSONObject location = a.getJSONObject("location");
        JSONObject addressComponents = a.getJSONObject("address_components");
        //经度
        String lng = location.getString("lng");
        //纬度
        String lat = location.getString("lat");
        //城市
        String city = addressComponents.getString("city");
        //可信度参考：值范围 1 <低可信> - 10 <高可信>,该值>=7时，解析结果较为准确
        String reliability = a.getString("reliability");
        //解析精度级别，分为11个级别，一般>=9即可采用（定位到点，精度较高
        String level = a.getString("level");

        resultMap.put("lng", lng);
        resultMap.put("lat", lat);
        resultMap.put("city", city);
        resultMap.put("reliability", reliability);
        resultMap.put("level", level);

        return resultMap;
    }

    //调腾讯地图api接口方法
    private static String getResult(String urlString) {
        String result = "";
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            // 腾讯地图使用GET
            conn.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            // 获取地址解析结果
            while ((line = in.readLine()) != null) {
                result += line + "\n";
                //System.out.println(result);
            }
            in.close();
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    /**
     * @Description: 通过城市获取行政区域
     * @Date: 2019/03/27
     */
    public static Map<String, Object> getArea(String keyword) {

        Map<String, Object> resultMap = new HashMap<>();

        // 参数解释：keyword：城市关键词。key：腾讯地图key
        String urlString = "https://apis.map.qq.com/ws/district/v1/search?keyword=" +
                keyword + "&key=" + key;

        String result = getResult(urlString);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String status = jsonObject.getString("status");
        if (!"0".equals(status)) {
            resultMap.put("status", jsonObject.getString("message"));
            return resultMap;
        }
        JSONArray jsonArray = jsonObject.getJSONArray("result");
        List<String> regions = new ArrayList<>();
        JSONArray a = jsonArray.getJSONArray(0);
        JSONObject b = a.getJSONObject(0);
        String id = b.getString("id");

        //获取指定行政区划的子级行政区划
        String url = "https://apis.map.qq.com/ws/district/v1/getchildren?id=" +
                id + "&key=" + key;

        String result2 = getResult(url);
        JSONObject jsonObject2 = JSONObject.parseObject(result2);
        String message = jsonObject.getString("status");
        if (!"0".equals(message)) {
            resultMap.put("status", jsonObject.getString("message"));
            return resultMap;
        }
        JSONArray jsonArray2 = jsonObject2.getJSONArray("result");
        JSONArray jSONArray3 = jsonArray2.getJSONArray(0);
        for (int i = 0; i < jSONArray3.size(); i++) {
            JSONObject region = jSONArray3.getJSONObject(i);
            String fullName = region.getString("fullname");
            regions.add(fullName);
        }


        resultMap.put("regions", regions);

        return resultMap;
    }

    /**
     * @Description: 通过ip获取经纬度, 位置
     * @return: Map
     * @Date: 2019/03/27
     */
    public static Map<String, Object> getlngAndlatByIp(String ip) {

        Map<String, Object> resultMap = new HashMap<>();

        // 参数解释：address 地址。key：腾讯地图key
        String urlString = "https://apis.map.qq.com/ws/location/v1/ip?ip=" +
                ip + "&key=" + key;

        String result = getResult(urlString);

        // 转JSON格式
        JSONObject jsonObject = JSONObject.parseObject(result);
        String status = jsonObject.getString("status");
        if (!"0".equals(status)) {
            resultMap.put("status", jsonObject.getString("message"));
            return resultMap;
        }
        JSONObject a = jsonObject.getJSONObject("result");
        JSONObject location = a.getJSONObject("location");
        JSONObject adInfo = a.getJSONObject("ad_info");
        //经度
        String lng = location.getString("lng");
        //纬度
        String lat = location.getString("lat");
        //城市
        String city = adInfo.getString("city");
        //区域
        String district = adInfo.getString("district");
        //行政区域代码
        String adcode = adInfo.getString("adcode");


        resultMap.put("lng", lng);
        resultMap.put("lat", lat);
        resultMap.put("city", city);
        resultMap.put("district", district);
        resultMap.put("adcode", adcode);

        return resultMap;
    }

    /**
     * @Description: 根据经度纬度 获取距离和时间（经度与纬度用英文逗号分隔，坐标间用英文分号分隔）
     * 注意：本服务支持单起点到多终点，或多起点到单终点，from和to参数仅可有一个为多坐标
     * from数据格式：from=39.983171,116.308479
     * to数据格式 to=39.996060,116.353455;39.949227,116.394310
     * @return: Map
     * @Author: KingYiFan
     * @Date: 2019/03/27
     */
    public static Map<String, Object> getDistanceAndTime(String from, List<String> toAdd) {

        Map<String, Object> resultMap = new HashMap<>();
        List<HashMap<String, Object>> list = new ArrayList<>();
        if (toAdd.size() < 1) {
            resultMap.put("status", "终点地址少于一个");
            return resultMap;
        }
        String to = "";
        for (String s : toAdd) {
            to += s + ";";
        }
        to = to.substring(0, to.length() - 1);

        // 参数解释：mode：计算方式（驾车）key：腾讯地图key from起点坐标，格式：lat,lng;lat,lng... to 终点坐标，格式：lat,lng;lat,lng...
        String urlString = "https://apis.map.qq.com/ws/distance/v1/?mode=driving&from=" + from + "&to=" + to + "&key=" + key;

        String jsonResult = getResult(urlString);

        // 转JSON格式
        JSONObject jsonObject = JSONObject.parseObject(jsonResult);
        String status = jsonObject.getString("status");
        if (!"0".equals(status)) {
            resultMap.put("status", jsonObject.getString("message"));
            return resultMap;
        }
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray elements = result.getJSONArray("elements");
        for (Object element : elements) {
            Map map = (Map) element;
            Map toMap = (Map) map.get("to");
            BigDecimal lat = (BigDecimal) toMap.get("lat");
            BigDecimal lng = (BigDecimal) toMap.get("lng");
            Integer distance = (Integer) map.get("distance");
            Double dis = Math.round(distance / 100d) / 10d;
            Integer duration = (Integer) map.get("duration");
            HashMap<String, Object> rmap = new HashMap<>();
            rmap.put("toLat", lat);
            rmap.put("toLng", lng);
            rmap.put("distance", dis);
            rmap.put("duration", duration / 60);
            list.add(rmap);
        }
        resultMap.put("list", list);
        return resultMap;
    }


    public static void main(String[] args) {
        // 测试1
//        String lat = "39.974880";//维度
//        String lng = "116.496790";//经度
//        Map<String, Object> map = getLocation(lng, lat);
//        System.out.println("shortAddName：" + map.get("shortAddName"));
//        System.out.println("longAddName：" + map.get("longAddName"));


        // 测试2
//        String address = "北京市西二旗地铁站";
//        Map<String, Object> map = getlngAndlat(address);
//        System.out.println(map);
//        System.out.println("lng：" + map.get("lng"));
//        System.out.println("lat：" + map.get("lat"));
//        System.out.println("city：" + map.get("city"));
//        System.out.println("reliability：" + map.get("reliability"));
//        System.out.println("level：" + map.get("level"));

//        // 测试3
//        String keyword = "成都市";
//        Map<String, Object> map = getArea(keyword);
//        System.out.println(map);
//        List<String> list = (List<String>)map.get("regions");
//        for(String str:list){
//            System.out.println(str);
//        }

        // 测试4
//        String ip = "192.168.129.14";
//
//        Map<String, Object> map = getlngAndlatByIp(ip);
//        System.out.println(map);
//        System.out.println("lng：" + map.get("lng"));
//        System.out.println("lat：" + map.get("lat"));
//        System.out.println("city：" + map.get("city"));
//        System.out.println("district：" + map.get("district"));
//        System.out.println("adcode：" + map.get("adcode"));


        // 测试4
        List<String> toAdd = new ArrayList<>();
        toAdd.add("39.996060,116.353455");
        toAdd.add("39.949227,116.394310");

        String from = "39.983171,116.308479";

        Map<String, Object> map = getDistanceAndTime(from, toAdd);
        List<HashMap<String, Object>> list = (List<HashMap<String, Object>>) map.get("list");
        for (HashMap<String, Object> stringObjectHashMap : list) {
            System.out.println("纬度：" + stringObjectHashMap.get("toLat"));
            System.out.println("经度" + stringObjectHashMap.get("toLng"));
            System.out.println("距离" + stringObjectHashMap.get("distance") + "公里");
            System.out.println("时间" + stringObjectHashMap.get("duration") + "分钟");
        }
    }
}
