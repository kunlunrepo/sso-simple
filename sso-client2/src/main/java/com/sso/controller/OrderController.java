package com.sso.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * description : 订单
 *
 * @author kunlunrepo
 * date :  2024-05-20 09:59
 */
@Controller
@Slf4j
public class OrderController {

    /**
     * 查询接口
     */
    @GetMapping(value = "/query")
    public String query(
            HttpSession session,
            @RequestParam(value = "token",required = false) String token
    ){
        // 判断是否传token
        if (null != token && !"".equals(token)){
            session.setAttribute("userLogin","zhang");
        }
        // 获取用户信息
        Object userLogin = session.getAttribute("userLogin");
        if (null != userLogin) {
            log.error("client：用户认证成功跳转至目标页面");
            return "order";
        }
        // 无认证则跳转登录页面
        log.error("client：用户未认证跳转至登录页面");
        return "redirect:http://localhost:8080/loginPage?redirectUrl=http://localhost:8082/query";
    }
}
