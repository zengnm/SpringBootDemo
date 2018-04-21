package com.example.controller;

import java.util.Date;

import javax.annotation.Resource;

import com.example.model.OrderEntity;
import com.example.service.OrderService;
import com.example.util.WebHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("userName", WebHelper.getPin());
        model.addAttribute("navigate", 0);

        return "index";
    }

    @RequestMapping("/api")
    @ResponseBody
    public String hello() {
        return "Hello World!" + WebHelper.getPin() + "," + WebHelper.getIpAddress();
    }



    @RequestMapping("/list")
    public String selectPage(Model model, OrderEntity entity,
                             @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize) {
        entity.setUserPin(!StringUtils.isEmpty(entity.getUserPin()) ? entity.getUserPin() : null);
        PageInfo<OrderEntity> pageInfo = orderService.selectPage(pageNum, pageSize, entity);
        model.addAttribute("userPin", entity.getUserPin());
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("userName", WebHelper.getPin());
        model.addAttribute("navigate", 1);
        model.addAttribute("subNavigate", "order");
        return "order/list";
    }

    @RequestMapping("/edit")
    public String edit(Model model, long id, String userPin, String orderPrice) {
        OrderEntity entity = new OrderEntity();
        entity.setId(id);
        entity.setUserPin(userPin);
        entity.setOrderPrice(orderPrice);
        entity.setModified(new Date());
        orderService.updateById(entity);
        OrderEntity queryEntity = new OrderEntity();
        return selectPage(model, queryEntity, 1, 10);
    }

    @RequestMapping("/doEdit")
    public String edit(Model model, Integer pageNum, Integer pageSize, OrderEntity entity) {
        orderService.updateById(entity);
        entity.setId(null);
        return selectPage(model, entity, 1, 10);
    }

    @RequestMapping("/delete")
    public String delete(Model model, long id, OrderEntity entity) {
        orderService.deleteById(id);
        entity.setId(null);
        return selectPage(model, entity, 1, 10);
    }
}
