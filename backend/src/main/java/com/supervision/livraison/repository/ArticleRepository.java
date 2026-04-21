package com.supervision.livraison.repository;

import com.supervision.livraison.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, String> {
}
