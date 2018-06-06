package com.yit.gitprd.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yit.gitprd.pojo.gitlab.Branch;
import org.junit.Test;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
public class MethodTest {

    @Test
    public void jsonToPojoTest() {
        String jsonContent = "{\n" +
                "    \"name\": \"master\",\n" +
                "    \"merged\": false,\n" +
                "    \"protected\": true,\n" +
                "    \"developers_can_push\": false,\n" +
                "    \"developers_can_merge\": false,\n" +
                "    \"commit\": {\n" +
                "      \"author_email\": \"john@example.com\",\n" +
                "      \"author_name\": \"John Smith\",\n" +
                "      \"authored_date\": \"2012-06-27T05:51:39-07:00\",\n" +
                "      \"committed_date\": \"2012-06-28T03:44:20-07:00\",\n" +
                "      \"committer_email\": \"john@example.com\",\n" +
                "      \"committer_name\": \"John Smith\",\n" +
                "      \"id\": \"7b5c3cc8be40ee161ae89a06bba6229da1032a0c\",\n" +
                "      \"short_id\": \"7b5c3cc\",\n" +
                "      \"title\": \"add projects API\",\n" +
                "      \"message\": \"add projects API\",\n" +
                "      \"parent_ids\": [\n" +
                "        \"4ad91d3c1144c406e50c7b33bae684bd6837faf8\"\n" +
                "      ]\n" +
                "    }}";
        JSONObject jsonObject = JSON.parseObject(jsonContent);
        Branch branch = jsonObject.toJavaObject(Branch.class);

        System.out.println(branch);

    }
}
