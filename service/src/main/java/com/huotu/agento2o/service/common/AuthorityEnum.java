package com.huotu.agento2o.service.common;

import com.huotu.agento2o.common.ienum.ICommonEnum;

/**
 * Created by helloztt on 2016/5/9.
 */
public enum AuthorityEnum implements ICommonEnum {
    MANAGER_ROOT("ROLE_ROOT", "超级管理员"),
    AGENT_ROOT("ROLE_AGENT","代理商"),
    ROLE_SHOP("ROLE_SHOP","门店"),
    ROLE_SHOPDATA("ROLE_SHOPDATA","门店资料"),
    ROLE_ORDER("ROLE_ORDER","订单"),
    ROLE_PURCHASE("ROLE_PURCHASE","采购");
    private String code;
    private String value;

    AuthorityEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
