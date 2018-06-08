package com.yit.gitprd;

import com.yit.gitprd.service.GitApiService;
import com.yit.gitprd.service.GitHelper;
import com.yit.gitprd.utils.FileUtil;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 初始化加载
 *
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
@Component
public class Initialization implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(Initialization.class);

    @Autowired
    private GitHelper gitHelper;
    @Autowired
    private GitApiService gitApiService;

    @Override
    public void afterPropertiesSet() throws Exception {
        //创建工作目录
        createWorkSpace();
        //克隆prd主分支, 如果存在则不克隆
        cloneMasterBranch();
    }

    //-- private methods

    private void createWorkSpace() {
        //* ~/gitprd (rootPath)
        //* ~/gitprd/master (master branch)
        FileUtil.createFolders(gitHelper.getMasterPath());
        FileUtil.createFolders(gitHelper.getBranchesPath());
    }

    private void cloneMasterBranch() {
        String masterPath = gitHelper.getMasterPath();
        try {
            if (!gitApiService.respExist(masterPath)) {
                logger.info("cloneMasterBranch - 仓库不存在, 开始克隆仓库: {}", masterPath);
                gitApiService.cloneBranch(masterPath);
            } else {
                logger.info("cloneMasterBranch - 仓库已存在, 不用创建仓库: {}", masterPath);
            }
        } catch (GitAPIException e) {
            logger.error("cloneMasterBranch", e);
            //TODO: 推送错误消息
        }
    }
}
