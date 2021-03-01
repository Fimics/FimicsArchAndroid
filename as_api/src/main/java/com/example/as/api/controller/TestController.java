package com.example.as.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class TestController {
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Object hello() {
        return "hello1";
    }
}
