package com.yit.gitprd.service;

import com.yit.gitprd.cons.GitPrdCons;
import com.yit.gitprd.utils.SystemUtils;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
@Component
public class GitHelper {

    @Value("${git.uri}")
    private String uri;

    @Value("${git.user}")
    private String userName;

    @Value("${git.pwd}")
    private String password;

    @Value("${gitprd.cfg.root_path_name}")
    private String rootPathName;

    @Value("${gitprd.server_uri}")
    private String prdServiceURI;

    private UsernamePasswordCredentialsProvider credentialsProvider;

    public String getUri() {
        return uri;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRootPath() {
        String userHome = SystemUtils.getUserHome();
        return userHome + "/" + rootPathName;
    }

    public String getMasterPath() {
        return getRootPath() + "/" + GitPrdCons.MASTER_NAME;
    }
    public String getRemotePath() {
        return getRootPath() + "/" + GitPrdCons.REMOTE_NAME;
    }

    public String getBranchesPath() {
        return getRootPath() + "/" + GitPrdCons.BRANCHES_NAME;
    }

    public String getRootPathName() {
        return rootPathName;
    }

    public UsernamePasswordCredentialsProvider getCredentialsProvider() {
        if (credentialsProvider == null) {
            synchronized (UsernamePasswordCredentialsProvider.class) {
                if (credentialsProvider == null) {
                    credentialsProvider = new UsernamePasswordCredentialsProvider(userName, password);
                }
            }
        }
        return credentialsProvider;
    }

    public String getPrdServiceURI() {
        return prdServiceURI;
    }
}
