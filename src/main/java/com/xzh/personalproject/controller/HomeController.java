package com.xzh.personalproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author XZHH
 * @Description:
 * @create 2018/12/11 0011 14:20
 * @modify By:
 **/
@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping("/test")
    public ModelAndView test(ModelAndView modelAndView) {
        modelAndView.setViewName("index");
        modelAndView.addObject("name", "xiaoxiao");
        return modelAndView;
    }
}
