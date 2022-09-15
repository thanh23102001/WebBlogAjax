package com.example.webblogajax.Repository;

import com.example.webblogajax.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findAllByAuthor(String author);

    List<Blog> findAllByTitleContainingIgnoreCase(String title);

    List<Blog> findAllByAuthor_Name(String author);

    List<Blog> findAllByCategory_Name(String category);
}
