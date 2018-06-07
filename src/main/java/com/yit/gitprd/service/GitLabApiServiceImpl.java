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
import com.yit.gitprd.utils.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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

    private static final Logger logger = LoggerFactory.getLogger(GitLabApiServiceImpl.class);

    @Value("${gitlab.api.prefix}")
    private String apiPrefix;
    //id
    @Value("${gitlab.project.path}")
    private String projectPath;
    @Value("${gitlab.auth.private_token}")
    private String privateToken;


    @Override
    public List<Branch> branchList() throws GitlabServiceException {
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
            throw new GitlabServiceException(e);
        }
    }

    @Override
    public Branch getBranch(String branchName) throws GitlabServiceException {
        Assert.notNull(branchName, "PRD名称不能为空");
        try {
            //example: /projects/5/repository/branches/master
            String url = apiPrefix
                    .concat("/projects/")
                    .concat(projectPath)
                    .concat("/repository/branches/")
                    .concat(branchName);
            String response = HttpUtils.sendGet(url, privateToken);
            JSONObject jsonObject = JSON.parseObject(response);
            return jsonObject.toJavaObject(Branch.class);
        } catch (HttpException e) {
            throw new GitlabServiceException(e);
        }
    }

    @Override
    public Branch createBranch(String branchName, String ref) throws GitlabServiceException {
        Assert.notNull(branchName, "新PRD名称不能为空");
        Assert.notNull(ref, "依赖PRD名不能为空");
        try {
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
            throw new GitlabServiceException(e);
        }
    }

    @Override
    public void deleteRemoteBranch(String branchName) throws GitlabServiceException {
        Assert.notNull(branchName, "PRD名称不能为空");
        try {
            //example: /projects/:id/repository/branches/:branch
            String url = apiPrefix
                    .concat("/projects/")
                    .concat(projectPath)
                    .concat("/repository/branches/")
                    .concat(branchName);
            String response = HttpUtils.sendDelete(url, privateToken, null);
            JSONObject jsonObject = JSON.parseObject(response);
            String deletedBranch = jsonObject.getString("branch_name");
            if (!branchName.equals(deletedBranch))
                throw new GitlabServiceException("删除错误: " + deletedBranch);
        } catch (HttpException e) {
            throw new GitlabServiceException(e);
        }
    }

    @Override
    public void addTag(String tagName, String ref) throws GitlabServiceException {
        Assert.notNull(tagName, "tagName不能为空");
        try {
            //example: /projects/:id/repository/tags
            String url = apiPrefix
                    .concat("/projects/")
                    .concat(projectPath)
                    .concat("/repository/tags");
            Map<String, String> params = new HashMap<>();
            params.put("id", projectPath);
            params.put("tag_name", tagName);
            params.put("ref", ref);
            params.put("message", "deleted branch " + ref + "by " + SystemUtils.getUserName());
            String response = HttpUtils.sendPost(url, privateToken, params);
            logger.debug("addTag response={}", response);
        } catch (HttpException e) {
            throw new GitlabServiceException(e);
        }
    }

    @Override
    public Commit commit(PushCommit pushCommit) throws GitlabServiceException {

        Assert.notNull(pushCommit.getBranchName(), "PRD名不能为空");
        Assert.notNull(pushCommit.getCommitMessage(), "备注信息不能为空");
        Assert.notNull(pushCommit.getActions(), "提交内容不能为空");
        try {
            //example: /projects/:id/repository/commits
            String url = apiPrefix
                    .concat("/projects/")
                    .concat(projectPath)
                    .concat("/repository/commits");
            String commitMsg = SystemUtils.getUserName() + ":" + pushCommit.getCommitMessage();
            pushCommit.setCommitMessage(commitMsg);
            String payload = JSON.toJSON(pushCommit).toString();
            logger.debug("commit payload={}", payload);
            String response = HttpUtils.sendPostWithPayload(url, privateToken, payload);
            logger.debug("commit response={}", response);
            JSONObject jsonObject = JSON.parseObject(response);
            return jsonObject.toJavaObject(Commit.class);
        } catch (HttpException e) {
            throw new GitlabServiceException(e);
        }
    }

    @Override
    public Commit pull(String branchName) throws GitlabServiceException {
        Assert.notNull(branchName, "PRD名称不能为空");
        try {
            //example: /projects/:id/repository/commits/:sha
            String url = apiPrefix
                    .concat("/projects/")
                    .concat(projectPath)
                    .concat("/repository/commits/")
                    .concat(branchName);
            String response = HttpUtils.sendGet(url, privateToken);
            JSONObject jsonObject = JSON.parseObject(response);
            return jsonObject.toJavaObject(Commit.class);
        } catch (HttpException e) {
            throw new GitlabServiceException(e);
        }
    }
}
