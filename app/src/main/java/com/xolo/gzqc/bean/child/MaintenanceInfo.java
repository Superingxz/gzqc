package com.xolo.gzqc.bean.child;

/**
 * 维修项目详细信息
 */
public class MaintenanceInfo {


    /**
     * itemt_name : 发动机大修
     * status : 2
     * itemt_id :
     * bf_quoted_priced_id : jxstar7761
     */

    private String itemt_name;
    private String status;
    private String itemt_id;
    private String bf_quoted_priced_id;
    /**
     * itemt_code : 102
     * default_team_name : 机械组
     * default_team_id : 101
     */
    private String itemt_code;
    private String default_team_name;
    private String default_team_id;
   private String split_id;



    public String getSplit_id() {
        return split_id;
    }

    public void setSplit_id(String split_id) {
        this.split_id = split_id;
    }

    public String getItemt_name() {
        return itemt_name;
    }

    public void setItemt_name(String itemt_name) {
        this.itemt_name = itemt_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemt_id() {
        return itemt_id;
    }

    public void setItemt_id(String itemt_id) {
        this.itemt_id = itemt_id;
    }

    public String getBf_quoted_priced_id() {
        return bf_quoted_priced_id;
    }

    public void setBf_quoted_priced_id(String bf_quoted_priced_id) {
        this.bf_quoted_priced_id = bf_quoted_priced_id;
    }



    @Override
    public String toString() {
        return  "{\"itemt_id\":\""+itemt_id+"\",\"itemt_name\":\""+itemt_name+"\",\"team_id\":\""+default_team_id+"\",\"team_name\":\""+default_team_name+"\",\"split_id\":\""+split_id+"\",\"bf_quoted_priced_id\":\""+bf_quoted_priced_id+"\"}";
    }

    public String getItemt_code() {
        return itemt_code;
    }

    public void setItemt_code(String itemt_code) {
        this.itemt_code = itemt_code;
    }

    public String getDefault_team_name() {
        return default_team_name;
    }

    public void setDefault_team_name(String default_team_name) {
        this.default_team_name = default_team_name;
    }

    public String getDefault_team_id() {
        return default_team_id;
    }

    public void setDefault_team_id(String default_team_id) {
        this.default_team_id = default_team_id;
    }


}
