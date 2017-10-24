package com.zmsoft.TestTool.modle;

public class TbSelecterInfo {
    private String text;
    private int defaultTvColor;
    private int checkedTvColor;
    private int resource;

    /**
     * @param text
     * @param defaultTvColor
     * @param checkedTvColor
     */
    public TbSelecterInfo(String text, int defaultTvColor, int checkedTvColor,
                          int resource) {
        this.text = text;
        this.defaultTvColor = defaultTvColor;
        this.checkedTvColor = checkedTvColor;
        this.resource = resource;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDefaultTvColor() {
        return defaultTvColor;
    }

    public void setDefaultTvColor(int defaultTvColor) {
        this.defaultTvColor = defaultTvColor;
    }

    public int getCheckedTvColor() {
        return checkedTvColor;
    }

    public void setCheckedTvColor(int checkedTvColor) {
        this.checkedTvColor = checkedTvColor;
    }

    public int getresource() {
        return resource;
    }

    public void setresource(int resource) {
        this.resource = resource;
    }
}