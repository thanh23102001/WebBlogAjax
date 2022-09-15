package com.example.webblogajax.controller;

import com.example.webblogajax.Repository.CategoryRepository;
import com.example.webblogajax.model.Blog;
import com.example.webblogajax.service.BlogService;
import com.example.webblogajax.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/blogList")
    public ResponseEntity<List<Blog>> getBlogList(@RequestParam(value = "title", required = false) String title,
                                                  @RequestParam(value = "author", required = false) String author,
                                                  @RequestParam(value = "category", required = false) String category) {
        List<Blog> blog = new ArrayList<>();
        int i = 0;
        System.out.println("check" + author == null);
        if (title == null && author == null && category == null) {
            i = 1;
            blogService.getAll().forEach(blog::add);
        } else if (title != null && author == null && category == null) {
            i = 2;
            blogService.findBlogByTitle(title).forEach(blog::add);
        } else if (title == null && author != null && category == null){
            i = 3;
            blogService.findBlogByAuthor(author).forEach(blog::add);
        } else {
            i = 4;
            blogService.findBlogByCategory(category).forEach(blog::add);
        }

        if (blog.isEmmpty()) {
            if (i == 2)
                throw new NoDataFoundException("title");
            else if (i == 3)
                throw new NoDataFoundException("author");
            else {
                throw new NoDataFoundException("Category");
            }
        }
        return new ResponseEntity<>(blog, HttpStatus.OK);
    }

    @GetMapping("/blogList/{id}")
    public ResponseEntity<List<Blog>> getBlog() {
        return new ResponseEntity<List<Blog>>(blogService.getBlogList(), HttpStatus.OK);
    }


    @PostMapping("/blog/save")
    public ResponseEntity<Void> saveOrUpdateBlog(@RequestBody Blog blog){
        blogService.saveOrUpdateBlog(blog);
        categoryService.getAllCategories();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/blog/delete/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable("id") long id){
        blogService.deleteBlog(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }





}
