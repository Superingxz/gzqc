package com.xolo.gzqc.bean.child;

import java.util.List;

/**
 * Created by Administrator on 2016/12/17.
 */
public class Logistics {
    String EBusinessID;
    String ShipperCode;
    String Success;
    String LogisticCode;
    String State;
    List<Traces> Traces;

    public List<com.xolo.gzqc.bean.child.Traces> getTraces() {
        return Traces;
    }

    public void setTraces(List<com.xolo.gzqc.bean.child.Traces> traces) {
        Traces = traces;
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }
}
