package com.xolo.gzqc.bean.child;

import com.xolo.gzqc.utils.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/22.
 */
public class ItemVerhaul implements Serializable{

    /**
     * itemt_name : 保养
     * team_name : 机械组
     * itemt_id : E86D7AC9-ECDB-41BE-ABD4-FAB034014C49
     * parts : [{"qty_unit":"个","parts_name":"空气格","qty":"1"}]
     */

    private String itemt_name;
    private String team_name;
    private String itemt_id;
    /**
     * qty_unit : 个
     * parts_name : 空气格
     * qty : 1
     */

    private String team_id;

    private List<PartsBean> parts = new ArrayList<>();

    public ItemVerhaul(String itemt_name, String team_name, String itemt_id, String team_id, List<PartsBean> parts) {
        this.itemt_name = itemt_name;
        this.team_name = team_name;
        this.itemt_id = itemt_id;
        this.team_id = team_id;
        this.parts = parts;
    }

    public String getTeam_id() {
        return team_id;
    }

    public void setTeam_id(String team_id) {
        this.team_id = team_id;
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

    public List<PartsBean> getParts() {
        return parts;
    }

    public void setParts(List<PartsBean> parts) {
        this.parts = parts;
    }


    @Override
//    {"itemt_name":"保养","team_name":"机械组","itemt_id":"E86D7AC9-ECDB-41BE-ABD4-FAB034014C49","parts":[{"qty_unit":"个","parts_name":"空气格","qty":"1"}],"team_id":"jxstar2677981"}
    public String toString() {
        if (parts == null){
            parts= new ArrayList<>();
        }


        return "{\"itemt_name\":\""+itemt_name+"\",\"team_name\":\""+team_name+"\",\"itemt_id\":\""+itemt_id+"\",\"parts\":"+parts.toString()+",\"team_id\":\""+team_id+"\"}";
    }

    public static class PartsBean implements Serializable{
        private String qty_unit;
        private String parts_name;
        private String qty;

        public PartsBean(String qty_unit, String parts_name, String qty) {
            this.qty_unit = qty_unit;
            this.parts_name = parts_name;
            this.qty = qty;
        }


        @Override
        public String toString() {
            return "{\"qty_unit\":\""+qty_unit+"\",\"parts_name\":\""+parts_name+"\",\"qty\":\""+qty+"\"}";
        }

        public String getQty_unit() {
            return qty_unit;
        }

        public void setQty_unit(String qty_unit) {
            this.qty_unit = qty_unit;
        }

        public String getParts_name() {
            return parts_name;
        }

        public void setParts_name(String parts_name) {
            this.parts_name = parts_name;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }
    }
}
