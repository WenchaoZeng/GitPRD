package com.yit.gitprd.utils;

import com.alibaba.fastjson.JSON;
import com.yit.gitprd.cons.GitLabCons;
import com.yit.gitprd.cons.GitLabReturnCode;
import com.yit.gitprd.exception.GitlabServiceException;
import com.yit.gitprd.exception.HttpException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Optional;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private final static int TIME_OUT = 30 * 1000;

    private final static int SUCCESS_CODE = 200;
    private final static int BAD_REQUEST_CODE = 400;
    private final static int UNAUTHORIZED_CODE = 401;
    private final static int METHOD_NOT_ALLOWED_CODE = 405;
    private final static int INNER_ERR_CODE = 500;

    private final static String AUTH_ERR_MSG = "接口授权失败";
    private final static String HTTP_STATUS_ERR_MSG = "HTTP状态异常";
    private final static String HTTP_ERR_MSG = "请求异常";
    private final static String INNER_ERR_MSG = "gitlab系统内部错误";
    private final static String HTTP_TIME_OUT_ERR_MSG = "gitlab接口超时";

    private HttpUtils() {
    }


    /**
     * 发送DELETE请求
     *
     * @param url
     * @param privateToken
     * @param params
     * @return
     * @throws HttpException
     * @throws GitlabServiceException
     */
    public static String sendDelete(String url, String privateToken, Map<String, String> params) throws HttpException, GitlabServiceException {
        return send(url, privateToken, params, Connection.Method.DELETE);
    }

    /**
     * 发送POST请求
     *
     * @param url
     * @param privateToken
     * @param params
     * @return
     * @throws HttpException
     * @throws GitlabServiceException
     */
    public static String sendPost(String url, String privateToken, Map<String, String> params) throws HttpException, GitlabServiceException {
        return send(url, privateToken, params, Connection.Method.POST);
    }


    /**
     * 发送get请求
     *
     * @param url
     * @param privateToken
     * @return
     * @throws HttpException
     * @throws GitlabServiceException
     */
    public static String sendGet(String url, String privateToken) throws HttpException, GitlabServiceException {
        return send(url, privateToken, null, Connection.Method.GET);
    }


    //---private method

    //获取连接
    private static Connection getConnection(String url, String privateToken) {
        return Jsoup.connect(url)
                .timeout(TIME_OUT)
                .header(GitLabCons.PRIVATE_TOKEN_HEADER_NAME, privateToken)
                .ignoreContentType(true)
                .ignoreHttpErrors(true);
    }


    //处理http状态码
    private static void handleHttpStatusCode(Connection.Response response) throws GitlabServiceException, HttpException {
        int statusCode = response.statusCode();
        if (SUCCESS_CODE != statusCode) {
            switch (statusCode) {
                case UNAUTHORIZED_CODE:
                    throw new GitlabServiceException(AUTH_ERR_MSG);
                case BAD_REQUEST_CODE: parseErrorMsg(response);
                case METHOD_NOT_ALLOWED_CODE: parseErrorMsg(response);
                case INNER_ERR_CODE:
                    throw new GitlabServiceException(INNER_ERR_MSG);
                default:
                    throw new HttpException(HTTP_STATUS_ERR_MSG + statusCode);
            }
        }
    }

    private static void parseErrorMsg(Connection.Response response) throws GitlabServiceException {
        String errorMsg = JSON.parseObject(response.body()).getString("message");
        Optional<GitLabReturnCode> gitLabReturnCode = GitLabReturnCode.parseByGitLabOriginalMsg(errorMsg);
        if (gitLabReturnCode.isPresent())
            throw new GitlabServiceException(gitLabReturnCode.get());
        else
            throw new GitlabServiceException(errorMsg);
    }

    private static String send(String url, String privateToken, Map<String, String> paramMap, Connection.Method method)
            throws HttpException, GitlabServiceException {
        try {
            Connection connection = getConnection(url, privateToken)
                    .method(method);
            if (paramMap != null) {
                connection.data(paramMap);
            }
            Connection.Response response = connection
                    .execute();
            handleHttpStatusCode(response);
            return response.body();
        } catch (GitlabServiceException | HttpException e) {
            throw e;
        } catch (SocketTimeoutException e) {
            throw new HttpException(HTTP_TIME_OUT_ERR_MSG);
        } catch (Exception e) {
            logger.error(method.name() + " Error", e);
            throw new HttpException(HTTP_ERR_MSG);
        }
    }
}
