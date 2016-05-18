/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 *
 */

package com.huotu.agento2o.service.service.purchase;

import com.huotu.agento2o.common.util.ApiResult;
import com.huotu.agento2o.service.entity.author.Author;
import com.huotu.agento2o.service.entity.purchase.AgentPurchaseOrder;

/**
 * Created by helloztt on 2016/5/18.
 */
public interface AgentPurchaseOrderService {

    ApiResult addPurchaseOrder(AgentPurchaseOrder purchaseOrder, Author author, String... shoppingCartIds);
}