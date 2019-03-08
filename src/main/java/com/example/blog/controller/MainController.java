package com.example.blog.controller;

import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String main(ModelMap map){
        map.addAttribute("allPosts", postRepository.findAll());
        map.addAttribute("allCategory", categoryRepository.findAll());
        return "index";
    }

}
