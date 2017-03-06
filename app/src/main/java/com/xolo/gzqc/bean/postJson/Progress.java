package com.xolo.gzqc.bean.postJson;

/**
 * 进度查询
 */
public class Progress {


    private String  is_finished ;
    /**
     * itemt_name : 发动机大修
     * team_name : 机械组
     * itemt_id : 101
     * team_id : 101
     * set_finished : 0
     * bf_assign_work_id : 67B0BA3E-C64D-4959-A563-69723455B701
     */

    private String itemt_name;
    private String team_name;
    private String itemt_id;
    private String team_id;
    private String set_finished;
    private String bf_assign_work_id;




    public String getIs_finished() {
        return is_finished;
    }

    public void setIs_finished(String is_finished) {
        this.is_finished = is_finished;
    }



    @Override
    public String toString() {
        return "{\"assign_work_id\":\""+bf_assign_work_id+"\",\"is_finished\":\""+set_finished+"\"}";
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
}
