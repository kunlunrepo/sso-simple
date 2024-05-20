package com.sso.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * description : 登录接口
 * SSO过程：
 * @author kunlunrepo
 * date :  2024-05-19 18:48
 */
@Controller
@Slf4j
public class LoginController {

    /**
     * 客户端跳转至sso登录页面
     */
    @GetMapping(value = "/loginPage")
    public String loginPage(
            @RequestParam(value = "redirectUrl",required = false) String url, // 跳转路径
            Model model,
            @CookieValue(value = "ssoToken",required = false) String token // ssoToken
    ){
        log.info("server：客户端--->sso登录页面 url:{}, token:{}", url, token);
        // 判断登录状态
        if (token != null){
            // 直接携带已有token跳转回去
            return "redirect:" + url + "?token=" + token;
        }
        // 跳转登录页面
        model.addAttribute("redirectUrl",url);
        return "login";
    }

    /**
     * sso登录页面跳转至客户端
     */
    @PostMapping(value = "/ssoLoginPage")
    public String ssoLoginPage(
            @RequestParam("userName") String userName, // 登录名
            @RequestParam("password") String password, // 密码
            @RequestParam(value = "redirectUrl",required = false) String url, // 跳转路径
            HttpServletResponse response // 响应
    ){
        log.info("server：sso登录页面--->客户端 userName:{}, password:{}, url:{}",userName,password, url);
        // 判断登录状态
        if("zhang".equals(userName) && "123".equals(password)){
            // 生成token
            String token = UUID.randomUUID().toString().replace("-", "")+":zhang";
            // 存储token
            Cookie cookie = new Cookie("ssoToken",token);
            response.addCookie(cookie);
            // 登录成功 -> 跳转至对应服务
            log.info("server：sso登录页面--->客户端 验证成功，跳转至目标页面");
            return "redirect:" + url + "?token=" + token;
        }
        // 跳转登录页面
        log.info("server：sso登录页面--->客户端 验证失败，返回登录页面");
        return "login";
    }

}
