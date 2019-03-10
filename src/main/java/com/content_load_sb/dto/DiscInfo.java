package com.content_load_sb.dto;

import java.io.Serializable;

public class DiscInfo implements Serializable{
    private String discPath;
    private String discType;

    public String getDiscPath() {
        return discPath;
    }

    public void setDiscPath(String discPath) {
        this.discPath = discPath;
    }

    public String getDiscType() {
        return discType;
    }

    public void setDiscType(String discType) {
        this.discType = discType;
    }
}
