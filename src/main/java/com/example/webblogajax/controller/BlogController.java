package com.example.webblogajax.controller;

import com.example.webblogajax.model.Blog;
import com.example.webblogajax.model.BlogForm;
import com.example.webblogajax.model.Category;
import com.example.webblogajax.model.Cover;
import com.example.webblogajax.service.BlogService;
import com.example.webblogajax.service.CategoryService;
import com.example.webblogajax.service.CoverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @Autowired
    private CoverService coverService;

    @Value("${upload.path}")
    private String fileUpload;

    @GetMapping("/blogList")
    public ResponseEntity<List<Blog>> getBlogList(@RequestParam(value = "title", required = false) String title,
                                                  @RequestParam(value = "author", required = false) String author,
                                                  @RequestParam(value = "category", required = false) String category) {
        List<Blog> blog = new ArrayList<>();
        int i = 0;
        System.out.println("check" + author == null);
        if (title == null && author == null && category == null) {
            i = 1;
            blogService.getBlogList().forEach(blog::add);
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

//        if (blog.isEmmpty()) {
//            if (i == 2)
//                throw new NoDataFoundException("title");
//            else if (i == 3)
//                throw new NoDataFoundException("author");
//            else {
//                throw new NoDataFoundException("Category");
//            }
//        }
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

    private void addFile(List<MultipartFile> files, Blog blog) {
        for (MultipartFile file : files) {
            try {
                var fileName = file.getOriginalFilename();
                var is = file.getInputStream();
                Files.copy(is, Paths.get(this.fileUpload + fileName), StandardCopyOption.REPLACE_EXISTING);
                Cover cover = new Cover(fileName, blog);
                coverService.save(cover);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("/blogs")
    public ResponseEntity<Blog> createBlog(@ModelAttribute BlogForm blogForm,
                                           BindingResult result) {
        System.out.println("add blog running...");
        System.out.println(blogForm.getCategory());
        Category category = categoryService.getCategory(blogForm.getCategory().getId());
        System.out.println(category.getName());
        blogForm.setCategory(category);
        System.out.println(blogForm.getTitle());
        if (result.hasErrors()) {
            for (Object object : result.getAllErrors()) {
                if(object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;

                    /**
                     * Use null as second parameter if you do not use i18n (internationalization)
                     */
//                    String message = messageSource.getMessage(fieldError, null);
//                    System.out.println(message);
                }
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Blog blog = new Blog.BlogBuilder(blogForm.getTitle())
                .content(blogForm.getContent())
                .build();
//        Category category1 = blog.getCategory();
        blog.setCategory(category);
        blogService.saveOrUpdateBlog(blog);
        addFile(blogForm.getFiles(), blog);

        return new ResponseEntity<>(blog, HttpStatus.CREATED);
    }

}
