package com.example.webblogajax.Repository;

import com.example.webblogajax.model.Blog;
import com.example.webblogajax.model.Cover;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoverRepository extends CrudRepository<Cover, Long> {
    void deleteByBlog(Blog blog);

//    @Query("DELETE FROM Cover Where Cover.blog.id = :blog_id")
//    void removeCover(@Param("blog_id") long blog_id);

    void removeCoverByBlog_Id(Long blog_id);
}