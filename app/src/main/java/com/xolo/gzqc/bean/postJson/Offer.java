package com.xolo.gzqc.bean.postJson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 报价单
 */
public class Offer implements Serializable{

    /**
     * itemt_id : 1
     * itemt_name : 发动机大修
     * hours : 3
     * workamt : 500
     * repairlist : [{"parts_id":"1","parts_name":"活塞","qty":"5","costprice":"120","saleprice":"200"},{"parts_id":"1","parts_name":"活塞环","qty":"3","costprice":"120","saleprice":"200"}]
     */

    private String itemt_id = "";
    private String itemt_name;
    private String hours;
    private String workamt;

    private String source_id;
    private String source;

    /**
     * parts_id : 1
     * parts_name : 活塞
     * qty : 5
     * costprice : 120
     * saleprice : 200
     */

    private List<Part> repairlist = new ArrayList<>();

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    /**
     * bf_quoted_priced_id : 4E00242A-031F-400F-B157-0C5C1F1E24F3
     */




    private String bf_quoted_priced_id = "";

    public String getItemt_id() {
        return itemt_id;
    }

    public void setItemt_id(String itemt_id) {
        this.itemt_id = itemt_id;
    }

    public String getItemt_name() {
        return itemt_name;
    }

    public void setItemt_name(String itemt_name) {
        this.itemt_name = itemt_name;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getWorkamt() {
        return workamt;
    }

    public void setWorkamt(String workamt) {
        this.workamt = workamt;
    }

    public List<Part> getRepairlist() {
        return repairlist;
    }

    public void setRepairlist(List<Part> repairlist) {
        this.repairlist = repairlist;
    }

    public String getBf_quoted_priced_id() {
        return bf_quoted_priced_id;
    }

    public void setBf_quoted_priced_id(String bf_quoted_priced_id) {
        this.bf_quoted_priced_id = bf_quoted_priced_id;
    }


    @Override
//    {"itemt_id": "1","itemt_name": "发动机大修","hours": "3","workamt": "500","repairlist": [{"parts_id": "1","parts_name": "活塞","qty": "5","costprice": "120","saleprice": "200"}]}
    public String toString() {
        return "{\"itemt_name\": \""+itemt_name+"\",\"itemt_id\":\""+itemt_id+"\",\"workamt\": \""+workamt+"\",\"repairlist\":"+repairlist.toString()+",\"bf_quoted_priced_id\":\""+bf_quoted_priced_id+"\"}";
    }

}
