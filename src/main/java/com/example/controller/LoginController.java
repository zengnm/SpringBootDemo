package com.example.controller;

import javax.servlet.http.HttpServletResponse;

import com.example.model.UserEntity;
import com.example.util.WebHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    @RequestMapping(value = {"login"}, method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("user", new UserEntity());
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String login(UserEntity user, RedirectAttributes redirect, HttpServletResponse response) {
        if("demo".equalsIgnoreCase(user.getName()) && "demo".equals(user.getPassword())){
            WebHelper.setCookie(response, user.getName());
            return "redirect:list";
        }else{
            redirect.addFlashAttribute("user", user);
            redirect.addFlashAttribute("message", "账号或密码错误");
            return "redirect:/login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpServletResponse httpServletResponse) {
        WebHelper.delCookie(httpServletResponse);
        return "redirect:login";
    }
}
