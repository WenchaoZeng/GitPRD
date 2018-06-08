package com.yit.gitprd.pojo.git;

import org.eclipse.jgit.lib.Ref;

/**
 * @author: clive
 * @date: 2018/06/08
 * @since: 1.0
 */
public class Branch {

    private Ref ref;
    private String name;



    //-----

    public Ref getRef() {
        return ref;
    }

    public void setRef(Ref ref) {
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
