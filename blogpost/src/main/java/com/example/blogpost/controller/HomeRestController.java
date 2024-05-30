package com.example.blogpost.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomeRestController {

    // @Autowired
    // private PostService postService;

    Logger logger = LoggerFactory.getLogger(HomeRestController.class);

    // @GetMapping("/")
    // public List<Post> home() {
    //     logger.debug("hello this the test on logger is woring or not");
    //     // return postService.findAll();
    //     return "hello";
    // }
    @GetMapping("/")
    public String home() {
        logger.debug("hello this the test on logger is woring or not");
        // return postService.findAll();
        return "hello";
    }
}
