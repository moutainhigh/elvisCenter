/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 *
 */

package com.huotu.agento2o.service.searchable;

import lombok.Data;

/**
 * Created by helloztt on 2016/5/18.
 */
@Data
public class PurchaseOrderSearcher {
    Integer agentId;

    Integer parentAgentId;
    /**
     * 审核状态
     */
    Integer statusCode;
    /**
     * 发货状态
     */
    Integer shipSatatusCode;
    /**
     * 付款状态
     */
    Integer paymentCode;
    /**
     * 采购单ID
     */
    String pOrderId;
    /**
     * 采购下单时间查询
     */
    private String beginTime;
    private String endTime;


}