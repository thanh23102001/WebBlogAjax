package com.example.webblogajax.service;

import com.example.webblogajax.Repository.CoverRepository;
import com.example.webblogajax.model.Blog;
import com.example.webblogajax.model.Cover;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CoverService {
    @Autowired
    private CoverRepository coverRepository;

    public Cover save(Cover cover) {
        return coverRepository.save(cover);
    }

    public void remove(Blog blog) {
        coverRepository.removeCoverByBlog_Id(blog.getId());
    }
}
