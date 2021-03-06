/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼在地图中查看
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 *
 */

package com.huotu.agento2o.service.repository.goods;

import com.huotu.agento2o.service.entity.goods.MallProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by allan on 3/21/16.
 */
@Repository
public interface MallProductRepository extends JpaRepository<MallProduct, Integer> {

    List<MallProduct> findByGoods_GoodsId(Integer goodsId);
}
