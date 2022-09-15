package com.example.webblogajax.service;


import com.example.webblogajax.Repository.AuthorRepository;
import com.example.webblogajax.Repository.BlogRepository;
import com.example.webblogajax.model.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public List<Blog> getBlogList(){
        return blogRepository.findAll();
    }

    public Blog getBlogById(Long id){
        return blogRepository.findById(id).get();
    }

    public void saveOrUpdateBlog(Blog blog){
        blogRepository.save(blog);
    }

    public void deleteBlog(long id){
        blogRepository.deleteById(id);
    }

    public List<Blog> findBlogByAuthor(String author) {
        return blogRepository.findAllByAuthor_Name(author);
    }

    public List<Blog> findBlogByCategory(String category) {
        return blogRepository.findAllByCategory_Name(category);
    }

    public List<Blog> findBlogByTitle(String title) {
        return blogRepository.findAllByTitleContainingIgnoreCase(title);
    }
}

