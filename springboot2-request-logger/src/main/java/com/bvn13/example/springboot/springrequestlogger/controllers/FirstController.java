package com.bvn13.example.springboot.springrequestlogger.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author bvn13
 * @since 29.09.2019
 */
@Controller
@RequestMapping(value = "/first")
public class FirstController {

    @GetMapping
    public String getFirst() {
        return "index";
    }

    @PostMapping
    public String postFirst() {
        return "redirect:/";
    }

}
