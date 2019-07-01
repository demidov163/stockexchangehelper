package com.demidov.projects.stockexhangehelper.data;

import lombok.Builder;

@Builder
public class StockShareParameters {
    private String shareName;
    private String fromDate;
    private String tillDate;
    private String attributeIndex;

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getTillDate() {
        return tillDate;
    }

    public void setTillDate(String tillDate) {
        this.tillDate = tillDate;
    }

    public String getAttributeIndex() {
        return attributeIndex;
    }

    public void setAttributeIndex(String attributeIndex) {
        this.attributeIndex = attributeIndex;
    }
}
