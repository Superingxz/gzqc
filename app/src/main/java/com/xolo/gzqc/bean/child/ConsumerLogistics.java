package com.xolo.gzqc.bean.child;

import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */
public class ConsumerLogistics {

    /**
     * EBusinessID : 1267420
     * ShipperCode : UC
     * Success : true
     * LogisticCode : 518336682846
     * State : 3
     * Traces : [{"AcceptTime":"2016-11-27 00:02:18","AcceptStation":"快件已由[海珠一部A]发往[广州分拨中心]","Remark":""},{"AcceptTime":"2016-11-27 00:02:18","AcceptStation":"[海珠一部A]【李晓菊】已收件","Remark":""},{"AcceptTime":"2016-11-27 01:59:25","AcceptStation":"快件已到达[广州分拨中心],上一站是[广州分拨中心]","Remark":""},{"AcceptTime":"2016-11-27 02:55:36","AcceptStation":"快件已由[广州分拨中心]发往[天河二部A]","Remark":""},{"AcceptTime":"2016-11-27 08:52:08","AcceptStation":"快件已到达[天河二部A],上一站是[广州分拨中心]","Remark":""},{"AcceptTime":"2016-11-27 09:39:19","AcceptStation":"[天河二部A]的【刘军军】正在派件, 电话：020-38271969 020-38075275","Remark":""},{"AcceptTime":"2016-11-28 14:51:15","AcceptStation":"快件已签收，签收人是【图片】","Remark":""}]
     */

    private String EBusinessID;
    private String ShipperCode;
    private boolean Success;
    private String LogisticCode;
    private String State;
    /**
     * AcceptTime : 2016-11-27 00:02:18
     * AcceptStation : 快件已由[海珠一部A]发往[广州分拨中心]
     * Remark :
     */

    private List<TracesBean> Traces;

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String ShipperCode) {
        this.ShipperCode = ShipperCode;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String LogisticCode) {
        this.LogisticCode = LogisticCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public List<TracesBean> getTraces() {
        return Traces;
    }

    public void setTraces(List<TracesBean> Traces) {
        this.Traces = Traces;
    }

    public static class TracesBean {
        private String AcceptTime;
        private String AcceptStation;
        private String Remark;

        public String getAcceptTime() {
            return AcceptTime;
        }

        public void setAcceptTime(String AcceptTime) {
            this.AcceptTime = AcceptTime;
        }

        public String getAcceptStation() {
            return AcceptStation;
        }

        public void setAcceptStation(String AcceptStation) {
            this.AcceptStation = AcceptStation;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String Remark) {
            this.Remark = Remark;
        }
    }
}

