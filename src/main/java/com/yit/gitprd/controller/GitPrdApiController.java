package com.yit.gitprd.controller;

import com.yit.gitprd.cons.GitPrdCons;
import com.yit.gitprd.pojo.ApiResponse;
import com.yit.gitprd.pojo.bean.BranchParam;
import com.yit.gitprd.service.GitPrdService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: clive
 * @date: 2018/06/05
 * @since: 1.0
 */

@RestController
@RequestMapping("api")
public class GitPrdApiController {

    private static final Logger logger = LoggerFactory.getLogger(GitPrdApiController.class);

    @Autowired
    private GitPrdService gitPrdService;

    @PostMapping("branches")
    public ApiResponse branches(@RequestBody BranchParam branchParam) {
        try {
            return ApiResponse.newSuccessInstance(gitPrdService.branches(branchParam.getBranchListType()));
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

    @PostMapping("create_branch")
    public ApiResponse createBranch(@RequestBody BranchParam branchParam) {
        try {
            gitPrdService.createBranch(branchParam.getBranchName(), branchParam.getRefBranchName());
            return ApiResponse.newSuccessInstance();
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

    @PostMapping("commit_modify")
    public ApiResponse commitModify(@RequestBody BranchParam branchParam) {
        try {
            gitPrdService.commitModify(branchParam.getBranchName(), branchParam.getComment());
            return ApiResponse.newSuccessInstance();
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

    @PostMapping("reset_modify")
    public ApiResponse resetModify(@RequestBody BranchParam branchParam) {
        try {
            gitPrdService.resetModify(branchParam.getBranchName());
            return ApiResponse.newSuccessInstance();
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

    @PostMapping("get_all_branch_status")
    public ApiResponse getAllBranchStatus() {
        try {
            return ApiResponse.newSuccessInstance(gitPrdService.getAllBranchStatus());
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

    @PostMapping("clone_branch")
    public ApiResponse cloneBranch(@RequestBody BranchParam branchParam) {
        try {
            gitPrdService.cloneBranch(branchParam.getBranchName());
            return ApiResponse.newSuccessInstance();
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

    @PostMapping("pull")
    public ApiResponse pull(@RequestBody BranchParam branchParam) {
        try {
            gitPrdService.pull(branchParam.getBranchName());
            return ApiResponse.newSuccessInstance();
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

    @PostMapping("delete")
    public ApiResponse delete(@RequestBody BranchParam branchParam) {
        try {
            gitPrdService.delete(branchParam.getBranchName());
            return ApiResponse.newSuccessInstance();
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

    @PostMapping("open_in_finder")
    public ApiResponse openInFinder(@RequestBody BranchParam branchParam) {
        try {
            gitPrdService.openInFinder(branchParam.getBranchName());
            return ApiResponse.newSuccessInstance();
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

    @PostMapping("get_online_url")
    public ApiResponse getOnlineUrl(@RequestBody BranchParam branchParam) {
        try {
            return ApiResponse.newSuccessInstance(gitPrdService.getOnlineUrl(branchParam.getBranchName()));
        } catch (IllegalArgumentException e) {
            return ApiResponse.newFailInstance(e.getMessage());
        } catch (GitAPIException e) {
            logger.error("GIT_API_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.GIT_API_ERROR);
        } catch (Exception e) {
            logger.error("SYS_ERROR", e);
            return ApiResponse.newFailInstance(GitPrdCons.SYS_ERROR);
        }
    }

}
