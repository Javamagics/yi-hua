package com.yihua.common;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 接入文心一言Api
 *
 * @author wangxusheng
 * @date 2023/12/18 17:43
 * @change 2023/12/18 17:43 by wangxusheng for init
 */
public class WenXinYiYanMethods {

    /**
     * 文心一言获取token的url地址
     */
    private static final String WEN_XIN_YI_YAN_TOKEN_URL = "https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=[api_key]&client_secret=[secret_key]";


    private static final String WEN_XIN_YI_YAN_CHAT_URL = "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions";

    /**
     * 获取文心一言token
     *
     * @return
     */
    public static String getToken() {
        String url = WEN_XIN_YI_YAN_TOKEN_URL;
        //将api_key 和 secret_key换成自己应用对应的
        url = url.replace("[api_key]", "B6jI3XuuTAjnzdXkqyXA0Y1f");
        url = url.replace("[secret_key]", "09qsgDmtIlF1vEGGsOd4EwbhMIGxGATB");
        String rep = HttpUtil.get(url);
        JSONObject repJsonObject = JSONObject.parseObject(rep);
        String token = String.valueOf(repJsonObject.get("access_token"));
        return token;
    }

    /**
     * 获取与文心的对话结果
     *
     * @param chatMessage
     * @return
     */
    public static String getChatResult(String chatMessage) {
        String url = WEN_XIN_YI_YAN_CHAT_URL;
        String accessToken = WenXinYiYanMethods.getToken();

        HashMap<String, String> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content",chatMessage);

        ArrayList<HashMap> messages = new ArrayList<>();
        messages.add(msg);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);

        String response = HttpUtil.post(url + "?access_token=" + accessToken, JSONUtil.toJsonStr(requestBody));

        JSONObject repJsonObject = JSONObject.parseObject(response);
        Object errorCode = repJsonObject.get("error_code");
        if (ObjectUtils.isNotEmpty(errorCode)) {
            return String.valueOf(repJsonObject.get("error_msg"));
        }
        return String.valueOf(repJsonObject.get("result"));
    }


}
