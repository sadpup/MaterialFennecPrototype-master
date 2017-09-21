package com.hongliang.li.ans;

import android.app.Application;
import android.util.SparseArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2017/9/21.
 */

public class FFApplication extends Application{

    public static SparseArray<String> mAutoTestDatas = new SparseArray<String>();

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            loadLocalData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @see prepare
     * */
    private void loadLocalData() throws Exception{
        StringBuffer sb = new StringBuffer();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("assets/" + "ac.json");
        BufferedReader buffin = new BufferedReader(new InputStreamReader(in));
        String temp = null;
        while(true){
            temp = buffin.readLine();
            if(temp == null)
                break;
            String tmp = new String(temp.getBytes(),"utf-8");
            sb.append(tmp);
        }
        in.close();
        buffin.close();
        JSONArray jsonArray = new JSONArray(sb.toString());
        for (int i =0;i<jsonArray.length();i++){
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String phrase = jsonObject.optString("phrase");
            mAutoTestDatas.put(i,phrase);
        }
    }

}
