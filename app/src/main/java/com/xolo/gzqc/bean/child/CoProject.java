package com.xolo.gzqc.bean.child;

import java.util.ArrayList;
import java.util.List;

/**
 * 维修清单
 * Created by Administrator on 2016/9/27.
 */
public class CoProject {
    String itemt_name;//项目名称
    String workamt;//价格
    List<ProjectChild> projectChildren;
    String bf_quoted_priced_id;
    List<CoAccessories> coAccessories;


    public List<CoAccessories> getCoAccessories() {
        return coAccessories;
    }

    public void setCoAccessories(List<CoAccessories> coAccessories) {
        this.coAccessories = coAccessories;
    }

    public String getBf_quoted_priced_id() {
        return bf_quoted_priced_id;
    }

    public void setBf_quoted_priced_id(String bf_quoted_priced_id) {
        this.bf_quoted_priced_id = bf_quoted_priced_id;
    }

    public CoProject(String itemt_name, String workamt) {
        this.itemt_name = itemt_name;
        this.workamt = workamt;
    }

    public String getItemt_name() {
        return itemt_name;
    }

    public void setItemt_name(String itemt_name) {
        this.itemt_name = itemt_name;
    }

    public String getWorkamt() {
        return workamt;
    }

    public void setWorkamt(String workamt) {
        this.workamt = workamt;
    }

    public List<ProjectChild> getProjectChildren() {
        return projectChildren;
    }

    public void setProjectChildren(List<ProjectChild> projectChildren) {
        this.projectChildren = projectChildren;
    }
}
