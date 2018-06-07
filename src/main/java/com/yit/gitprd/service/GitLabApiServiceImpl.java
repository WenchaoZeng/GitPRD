package com.yit.gitprd.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yit.gitprd.exception.GitlabServiceException;
import com.yit.gitprd.exception.HttpException;
import com.yit.gitprd.pojo.gitlab.Branch;
import com.yit.gitprd.pojo.gitlab.Commit;
import com.yit.gitprd.pojo.gitlab.PushCommit;
import com.yit.gitprd.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: clive
 * @date: 2018/06/06
 * @since: 1.0
 */
@Service
public class GitLabApiServiceImpl implements GitLabApiService {

    @Value("${gitlab.api.prefix}")
    private String apiPrefix;
    //id
    @Value("${gitlab.project.path}")
    private String projectPath;
    @Value("${gitlab.auth.private_token}")
    private String privateToken;


    @Override
    public List<Branch> branchList()throws GitlabServiceException {
        try {
            //example: /projects/5/repository/branches
            String url = apiPrefix
                    .concat("/projects/")
                    .concat(projectPath)
                    .concat("/repository/branches");
            String response = HttpUtils.sendGet(url, privateToken);
            JSONArray jsonArray = JSONArray.parseArray(response);
            return jsonArray.toJavaList(Branch.class);
        } catch (HttpException e) {
            throw new GitlabServiceException(e.getMessage());
        }
    }

    @Override
    public Branch getBranch(String branchName)throws GitlabServiceException {
        try {
            if (branchName == null) throw new GitlabServiceException("PRD名称不能为空");
            //example: /projects/5/repository/branches/master
            String url = apiPrefix
                    .concat("/projects/")
                    .concat(projectPath)
                    .concat("/repository/branches")
                    .concat(branchName);
            String response = HttpUtils.sendGet(url, privateToken);
            JSONObject jsonObject = JSON.parseObject(response);
            return jsonObject.toJavaObject(Branch.class);
        } catch (HttpException e) {
            throw new GitlabServiceException(e.getMessage());
        }
    }

    @Override
    public Branch createBranch(String branchName, String ref) throws GitlabServiceException {
        try {
            if (branchName == null) throw new GitlabServiceException("新PRD名称不能为空");
            if (ref == null) throw new GitlabServiceException("依赖PRD名不能为空");
            //example: /projects/:id/repository/branches
            String url = apiPrefix
                    .concat("/projects/")
                    .concat(projectPath)
                    .concat("/repository/branches");
            Map<String, String> params = new HashMap<>();
            params.put("branch_name", branchName);
            params.put("ref", ref);
            String response = HttpUtils.sendPost(url, privateToken, params);
            JSONObject jsonObject = JSON.parseObject(response);
            return jsonObject.toJavaObject(Branch.class);
        } catch (HttpException e) {
            throw new GitlabServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteRemoteBranch(String branchName) throws GitlabServiceException {
        try {
            if (branchName == null) throw new GitlabServiceException("PRD名称不能为空");
            //example: /projects/:id/repository/branches/:branch
            String url = apiPrefix
                    .concat("/projects/")
                    .concat(projectPath)
                    .concat("/repository/branches")
                    .concat(branchName);
            String response = HttpUtils.sendDelete(url, privateToken, null);
            JSONObject jsonObject = JSON.parseObject(response);
            String deletedBranch = jsonObject.getString("branch_name");
            if (!branchName.equals(deletedBranch))
                throw new GitlabServiceException("删除错误: " + deletedBranch);
        } catch (HttpException e) {
            throw new GitlabServiceException(e.getMessage());
        }
    }

    @Override
    public void addTag(String tagName, String ref, String msg) throws GitlabServiceException {

    }

    @Override
    public void commit(PushCommit pushCommit) throws GitlabServiceException {

    }

    @Override
    public Commit pull(String branch) {
        return null;
    }
}
