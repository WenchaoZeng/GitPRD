package com.yit.gitprd.test;

import com.yit.gitprd.utils.HttpUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
public class HttpUtilsTest {

    private static final String ID = "javayuanjian%2Fprd";
    private static final String PRIVATE_TOKEN = "_Wk4XsmrGzikACbLCtXR";

    @Test
    public void sendDeleteTest() throws Exception {
        String branchName = "onsa";
        String url = "http://gitlab.yit.com/api/v3/projects/" + ID + "/repository/branches/" + branchName;
        String response = HttpUtils.sendDelete(url, PRIVATE_TOKEN, null);
        System.out.println(response);
    }

    @Test
    public void sendPostTest() throws Exception {
        String url = "http://gitlab.yit.com/api/v3/projects/" + ID + "/repository/branches";

        Map<String, String> params = new HashMap<>();
        //params.put("id", ID);
        params.put("branch_name", "on_sale_1");
        params.put("ref", "master");
        String response = HttpUtils.sendPost(url, PRIVATE_TOKEN, params);
        System.out.println(response);
    }

    @Test
    public void sendGetTest() throws Exception {
        String url = "http://gitlab.yit.com/api/v3/projects/" + ID + "/repository/branches";
        String response = HttpUtils.sendGet(url, PRIVATE_TOKEN);
        System.out.println(response);
    }
}
