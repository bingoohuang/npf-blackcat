package com.github.bingoohuang.blackcat;

public class BlackcatContext {
    private String traceId;
    private String parentLinkId;
    private int subLinkId = 0;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getParentLinkId() {
        return parentLinkId;
    }

    public void setParentLinkId(String parentLinkId) {
        this.parentLinkId = parentLinkId;
    }

    public int getSubLinkId() {
        return subLinkId;
    }

    public int incrAndGetSubLinkId() {
        return ++subLinkId;
    }
}
