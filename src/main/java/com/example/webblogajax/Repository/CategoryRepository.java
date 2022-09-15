package com.example.webblogajax.Repository;

import com.example.webblogajax.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
