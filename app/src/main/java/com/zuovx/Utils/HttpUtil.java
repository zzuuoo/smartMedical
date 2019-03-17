package com.zuovx.Utils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    public static final String url = "http://www.baidu.com";
    private void postDataWithParame(String url,Map<String,String> map) {
//        Map<String,String> map = new HashMap<>();
//        map.put("playType","Test");//传入参数
        JSONObject jsonObject = new JSONObject(map);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，
//        String jsonStr = "{\"username\":\"lisi\",\"nickname\":\"李四\"}";//json数据.
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                AddPlayActivity.this.setResult(0);
//                finish();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if(response.body().string().equals("succeed"))
                {
//                    AddPlayActivity.this.setResult(1);
                }else{
//                    AddPlayActivity.this.setResult(0);
                }

//                finish();
            }


        });
//        return client;
    }

    /**
     * 将流转换成字符串
     *
     * @param is
     * @return
     */
    public static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the
         * BufferedReader.readLine() method. We iterate until the BufferedReader
         * return null which means there's no more data to read. Each line will
         * appended to a StringBuilder and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
