package com.xolo.gzqc.bean.child;

/**
 * Created by Administrator on 2016/10/14.
 */
public class CoMaintainDetile {
    String itemt_name;
    String team_name;
    String set_finished;

    public CoMaintainDetile(String itemt_name, String team_name, String set_finished) {
        this.itemt_name = itemt_name;
        this.team_name = team_name;
        this.set_finished = set_finished;
    }

    public String getItemt_name() {
        return itemt_name;
    }

    public void setItemt_name(String itemt_name) {
        this.itemt_name = itemt_name;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getSet_finished() {
        return set_finished;
    }

    public void setSet_finished(String set_finished) {
        this.set_finished = set_finished;
    }
}
