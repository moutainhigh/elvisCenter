package com.huotu.agento2o.agent.controller.order;

import com.huotu.agento2o.agent.config.annotataion.AgtAuthenticationPrincipal;
import com.huotu.agento2o.common.util.ApiResult;
import com.huotu.agento2o.common.util.Constant;
import com.huotu.agento2o.common.util.ResultCodeEnum;
import com.huotu.agento2o.service.common.OrderEnum;
import com.huotu.agento2o.service.entity.author.Author;
import com.huotu.agento2o.service.entity.author.Shop;
import com.huotu.agento2o.service.entity.order.MallDelivery;
import com.huotu.agento2o.service.entity.order.MallOrder;
import com.huotu.agento2o.service.entity.order.MallOrderItem;
import com.huotu.agento2o.service.entity.purchase.AgentProduct;
import com.huotu.agento2o.service.model.order.DeliveryInfo;
import com.huotu.agento2o.service.searchable.DeliverySearcher;
import com.huotu.agento2o.service.service.order.MallDeliveryService;
import com.huotu.agento2o.service.service.order.MallOrderItemService;
import com.huotu.agento2o.service.service.order.MallOrderService;
import com.huotu.agento2o.service.service.purchase.AgentProductService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by AiWelv on 2016/5/11.
 */

@Controller
@RequestMapping("/order")
@PreAuthorize("hasAnyRole('ORDER')")
public class DeliveryController {
    private static final Log log = LogFactory.getLog(DeliveryController.class);

    @Autowired
    private MallDeliveryService deliveryService;

    @Autowired
    private MallOrderService orderService;

    @Autowired
    private MallOrderItemService orderItemService;

    @Autowired
    private AgentProductService agentProductService;

    /**
     * 根据供应商和筛选条件查找发货单列表
     *
     * @param author
     * @return view:
     * "deliveryList"
     * model:
     * "deliveryList":List<OrdersDelivery>
     * "totalRecords":int
     * "totalPages":int
     * "pageSize":int
     * @throws Exception
     */
    @RequestMapping(value = "/deliveries", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('AGENT','SHOP','ORDER')")
    public ModelAndView showDeliveryList(
            @AgtAuthenticationPrincipal Author author,
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "delivery") String type,
            DeliverySearcher deliverySearcher
    ) throws Exception {
        ModelAndView modelAndView = new ModelAndView();
        deliverySearcher.setAgentId(author.getId());
        if (type.equals("delivery"))
            modelAndView.setViewName("order/delivery_list");
        else
            modelAndView.setViewName("order/return_list");

        Page<MallDelivery> ordersDeliveryPage = deliveryService.getPage(pageIndex,author, Constant.PAGESIZE,  deliverySearcher, type);
        modelAndView.addObject("deliveryList", ordersDeliveryPage.getContent());
        modelAndView.addObject("totalRecords", ordersDeliveryPage.getTotalElements());
        modelAndView.addObject("totalPages", ordersDeliveryPage.getTotalPages());
        modelAndView.addObject("shipModeEnums", OrderEnum.ShipMode.values());
        modelAndView.addObject("pageSize", ordersDeliveryPage.getSize());
        modelAndView.addObject("pageIndex", pageIndex);
        modelAndView.addObject("deliverySearcher", deliverySearcher);
        return modelAndView;
    }

    /**
     * 判断货品的库存是否满足订单所需的货品数量,其规则为
     * nums<=freez<=store
     * 满足则返回200失败返回505
     * @param orderId
     * @return
     */
    @RequestMapping(value = "/judgeStock" ,method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('AGENT','SHOP','ORDER')")
    @ResponseBody
    public ApiResult judgeStock( @AgtAuthenticationPrincipal Shop shop,
                                 String orderId){
        ApiResult apiResult = ApiResult.resultWith(ResultCodeEnum.INVENTORY_SHORTAGE, ResultCodeEnum.INVENTORY_SHORTAGE.getResultMsg(), null);
        MallOrder order = orderService.findByOrderId(orderId);
        List<MallOrderItem> mallOrderItems = orderItemService.findMallOrderItemByOrderId(order.getOrderId());
        AgentProduct agentProduct ;
        for (MallOrderItem mallOrderItem : mallOrderItems){
            agentProduct = agentProductService.findAgentProduct(shop,mallOrderItem.getProduct());
            if (agentProduct!=null && agentProduct.getFreez()>=mallOrderItem.getNums() && agentProduct.getFreez()<=agentProduct.getStore()){
                apiResult.setCode(ResultCodeEnum.SUCCESS.getResultCode());
                apiResult.setMsg(ResultCodeEnum.SUCCESS.getResultMsg());
                break;
            }
        }
        return apiResult;
    }

    /**
     * 加载发货单页面
     *
     * @param orderId
     * @param model
     * @return
     */
    @RequestMapping(value = "/delivery", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('AGENT','SHOP','ORDER')")
    public String showConsignFlow(@AgtAuthenticationPrincipal Shop shop,String orderId, Model model) {
        MallOrder order = orderService.findByOrderId(orderId);
        AgentProduct agentProduct ;
        for (int i=0; i<order.getOrderItems().size(); i++){
            agentProduct = agentProductService.findAgentProduct(shop,order.getOrderItems().get(i).getProduct());
            if (agentProduct!=null) {
                order.getOrderItems().get(i).setStore(agentProduct.getStore());
                order.getOrderItems().get(i).setFreez(agentProduct.getFreez());
                order.getOrderItems().get(i).setStockAdequate(agentProduct.getFreez() >= order.getOrderItems().get(i).getNums() && agentProduct.getFreez() <= agentProduct.getStore());
            }
        }
        model.addAttribute("order", order);
        return "order/delivery";
    }

    /**
     * 发货单保存接口
     * 未完成
     */
    @RequestMapping(value = "/delivery", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('SHOP','ORDER')")
    @ResponseBody
    public ApiResult addDelivery(
            @AgtAuthenticationPrincipal Shop shop,
            DeliveryInfo deliveryInfo
    ) throws Exception {
        return deliveryService.pushDelivery(deliveryInfo, shop.getId());
    }

}
