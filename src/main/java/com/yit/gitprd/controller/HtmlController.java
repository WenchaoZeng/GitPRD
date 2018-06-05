//package com.yit.gitprd.controller;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * @author: clive
// * @date: 2018/06/05
// * @since: 1.0
// */
//@RestController
//@RequestMapping("html")
//public class HtmlController {
//
//    private static final Logger logger = LoggerFactory.getLogger(HtmlController.class);
//
//    private static final String HTML_PATH = "/html/";
//
//    @GetMapping("/{htmlName}")
//    public String html(@PathVariable("htmlName") String htmlName) {
//        logger.info("htmlName={}", htmlName);
//        return HTML_PATH + htmlName;
//    }
//
//}
