package com.huotu.agento2o.service.service.purchase.impl;

import com.huotu.agento2o.common.util.SerialNo;
import com.huotu.agento2o.service.entity.author.Agent;
import com.huotu.agento2o.service.entity.author.Author;
import com.huotu.agento2o.service.entity.purchase.AgentProduct;
import com.huotu.agento2o.service.entity.purchase.AgentReturnedOrder;
import com.huotu.agento2o.service.repository.purchase.AgentReturnOrderItemRepository;
import com.huotu.agento2o.service.repository.purchase.AgentReturnOrderRepository;
import com.huotu.agento2o.service.searchable.ReturnedOrderSearch;
import com.huotu.agento2o.service.service.common.CommonTestBase;
import com.huotu.agento2o.service.service.purchase.AgentReturnedOrderService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * Created by wuxiongliu on 2016/5/18.
 */
public class AgentReturnedOrderServiceTest extends CommonTestBase {

    @Autowired
    private AgentReturnedOrderService agentReturnedOrderService;

    @Autowired
    private AgentReturnOrderRepository agentReturnOrderRepository;

    @Autowired
    private AgentReturnOrderItemRepository agentReturnOrderItemRepository;


    @Test
    public void testFindAgentProductsByAgentId(){
        Integer agentId = -1;
        List<AgentProduct> agentProducts =  agentReturnedOrderService.findAgentProductsByAgentId(agentId);
        Assert.assertTrue(agentProducts.size()==0);

        agentId = null;
        agentProducts =  agentReturnedOrderService.findAgentProductsByAgentId(agentId);
        Assert.assertTrue(agentProducts.size()==0);

        agentId = 1;
        agentProducts =  agentReturnedOrderService.findAgentProductsByAgentId(agentId);
        Assert.assertTrue(agentProducts.size()>=0);
    }

    @Test
    public void testFindAll(){
        ReturnedOrderSearch returnedOrderSearch = new ReturnedOrderSearch();

//        returnedOrderSearch.setAgentId(2);
        returnedOrderSearch.setParentAgentId(71);


        Author author = new Agent();
        author.setId(2);

        Page<AgentReturnedOrder> agentReturnedOrderPage = agentReturnedOrderService.findAll(1,10,author,returnedOrderSearch);

        List<AgentReturnedOrder> agentReturnedOrders = agentReturnedOrderPage.getContent();

        System.out.println("**************");
        agentReturnedOrderPage.forEach(agentReturnedOrder -> {
            System.out.println(agentReturnedOrder.getROrderId());
        });
        System.out.println("**************");

    }

    @Test
    public void testAddReturnOrder(){
        AgentReturnedOrder agentReturnedOrder = new AgentReturnedOrder();
        agentReturnedOrder.setROrderId(SerialNo.create());
        agentReturnedOrder.setCreateTime(new Date());
        agentReturnedOrderService.addReturnOrder(agentReturnedOrder);
    }

    @Test
    public void testCancelReturnOrder(){
        String rOrderId = "20160520094427115743";
        agentReturnedOrderService.cancelReturnOrder(rOrderId);
    }

    @Test
    public void testFindByRorderId(){
        String rOrderId = "20160520094427115743";
        AgentReturnedOrder agentReturnedOrder = agentReturnOrderRepository.findByROrderIdAndDisabledFalse(rOrderId);
        System.out.println(agentReturnedOrder);
    }

}
