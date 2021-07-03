package com.zyx.article.mapper;

import com.zyx.my.mapper.MyMapper;
import com.zyx.pojo.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapper extends MyMapper<Article> {
}