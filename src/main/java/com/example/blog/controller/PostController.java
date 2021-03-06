package com.example.blog.controller;

import com.example.blog.model.Category;
import com.example.blog.model.Post;
import com.example.blog.repository.CategoryRepository;
import com.example.blog.repository.PostRepository;
import javafx.geometry.Pos;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    @Value("${image.upload.dir}")
    private String imageUploadDir;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/post/add")
    public String addPost(ModelMap map) {
        map.addAttribute("allCategory", categoryRepository.findAll());
        return "addPost";
    }

    @PostMapping("/post/add")
    public String addPost(@ModelAttribute Post post, @RequestParam("picture") MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File picture = new File(imageUploadDir + File.separator + fileName);
        file.transferTo(picture);
        post.setPicUrl(fileName);
        post.setDate(new Date());
        postRepository.save(post);
        return "redirect:/post/add";
    }

    @GetMapping("/post/getImage")
    public void getImageAsByteArray(HttpServletResponse response, @RequestParam("picUrl") String picUrl) throws IOException {
        InputStream in = new FileInputStream(imageUploadDir + File.separator + picUrl);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        IOUtils.copy(in, response.getOutputStream());
    }

    @GetMapping("/post/inner")
    public String singlePost(ModelMap map, @RequestParam("id") int id) {
        Post post = postRepository.getOne(id);
        map.addAttribute("singlePost", post);
        return "inner";
    }

    @GetMapping("/post/by/category")
    public String postByCategory(ModelMap map, @RequestParam("id") int id) {
        Category optionalCategory = categoryRepository.getOne(id);

        List<Post> allpost = postRepository.findAll();
        List<Post> postByCategory = new ArrayList<>();

        for (Post post : allpost) {
            for (Category postCategory : post.getCategories()) {
                if (postCategory==optionalCategory ){
                    postByCategory.add(post);
                }

            }

        }
        map.addAttribute("postbyCategory",postByCategory);
        map.addAttribute("allCategory",categoryRepository.findAll());
        return"postByCategory";

    }

    @GetMapping("/post/delete")
    public String deleteById(@RequestParam("id") int id) {
        Optional<Post> one = postRepository.findById(id);
        if (one.isPresent()) {
            postRepository.deleteById(id);
        }
        return "redirect:/";
    }

}