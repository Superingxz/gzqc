package com.xolo.gzqc.bean.child;

/**
 * Created by ye on 2016/10/17.
 */
public class Complete {

    /**
     * modify_userid : jxstar6852949
     * itemt_name : 发动机大修
     * user_name : 接车测试2
     * is_repairing : 0
     * is_preparing : 1
     * team_name : 机械组
     * is_completed : 0
     * itemt_id : 101
     * team_id : 101
     * set_finished : 1
     * bf_assign_work_id : 67B0BA3E-C64D-4959-A563-69723455B701
     */

    private String modify_userid;
    private String itemt_name;
    private String user_name;
    private String is_repairing;
    private String is_preparing;
    private String team_name;
    private String is_completed;
    private String itemt_id;
    private String team_id;
    private String set_finished;
    private String bf_assign_work_id;

    public String getModify_userid() {
        return modify_userid;
    }

    public void setModify_userid(String modify_userid) {
        this.modify_userid = modify_userid;
    }

    public String getItemt_name() {
        return itemt_name;
    }

    public void setItemt_name(String itemt_name) {
        this.itemt_name = itemt_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getIs_repairing() {
        return is_repairing;
    }

    public void setIs_repairing(String is_repairing) {
        this.is_repairing = is_repairing;
    }

    public String getIs_preparing() {
        return is_preparing;
    }

    public void setIs_preparing(String is_preparing) {
        this.is_preparing = is_preparing;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(String is_completed) {
        this.is_completed = is_completed;
    }

    public String getItemt_id() {
        return itemt_id;
    }

    public void setItemt_id(String itemt_id) {
        this.itemt_id = itemt_id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
    }

    public String getSet_finished() {
        return set_finished;
    }

    public void setSet_finished(String set_finished) {
        this.set_finished = set_finished;
    }

    public String getBf_assign_work_id() {
        return bf_assign_work_id;
    }

    public void setBf_assign_work_id(String bf_assign_work_id) {
        this.bf_assign_work_id = bf_assign_work_id;
    }

    @Override
    public String toString() {
        return "{\"assign_work_id\":\""+bf_assign_work_id+"\",\"is_preparing\":\""+is_preparing+"\",\"is_repairing\":\""+is_repairing+"\",\"is_completed\":\""+is_completed+"\"}";
    }
}
