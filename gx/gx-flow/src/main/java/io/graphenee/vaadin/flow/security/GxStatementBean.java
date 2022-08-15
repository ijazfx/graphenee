package io.graphenee.vaadin.flow.security;

import java.util.regex.Matcher;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GxStatementBean {
    private String access;
    private String path;
    private boolean all;
    private boolean view;
    private boolean edit;
    private boolean delete;
    private boolean execute;

    public GxStatementBean makeStatementBean(Matcher matcher) {
        GxStatementBean gBean = new GxStatementBean();
        gBean.setAccess(matcher.group("permission"));
        gBean.setPath(matcher.group("resource"));
        gBean.setAll(matcher.group("action").trim().equals("all"));
        gBean.setDelete(
                matcher.group("action").trim().equals("all") ? true
                        : matcher.group("action").trim().equals("delete"));
        gBean.setEdit(
                matcher.group("action").trim().equals("all") ? true
                        : matcher.group("action").trim().equals("edit"));
        gBean.setView(
                matcher.group("action").trim().equals("all") ? true
                        : matcher.group("action").trim().equals("view"));
        gBean.setExecute(
                matcher.group("action").trim().equals("all") ? true
                        : matcher.group("action").trim().equals("execute"));
        return gBean;
    }
}
