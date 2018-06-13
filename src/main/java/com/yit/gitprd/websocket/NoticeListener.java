package com.yit.gitprd.websocket;

import com.yit.gitprd.cons.AppCons;
import com.yit.gitprd.cons.GitPrdCons;
import com.yit.gitprd.pojo.GitStatus;
import com.yit.gitprd.pojo.Notice;
import com.yit.gitprd.service.GitPrdService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 通知监听
 *
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
@Component
public class NoticeListener implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(NoticeListener.class);


    @Autowired
    private GitPrdService gitPrdService;
    @Autowired
    private NoticeSocket noticeSocket;

    @Override
    public void afterPropertiesSet() throws Exception {

        new Thread(()-> asyncRun()).start();

    }

    private void asyncRun() {
        while (true) {
            logger.info("noticeTask . . .");
            Notice notice;
            try {
                List<GitStatus> allBranchStatus = gitPrdService.getAllBranchStatus();
                notice = Notice.newBranchStatusNotice(allBranchStatus);
            } catch (IllegalArgumentException e) {
                notice = Notice.newMsgNotice(e.getMessage());
            } catch (GitAPIException e) {
                logger.error("getAllBranchStatus", e);
                notice = Notice.newMsgNotice(GitPrdCons.GIT_API_ERROR);
            } catch (Exception e) {
                logger.error("getAllBranchStatus", e);
                notice = Notice.newMsgNotice(GitPrdCons.SYS_ERROR);
            }
            noticeSocket.sendNotice(notice);
            try {
                Thread.sleep(AppCons.NOTICE_INTERVAL * 1000);
            } catch (InterruptedException e) {
                logger.error("InterruptedException", e);
            }
        }
    }


}
