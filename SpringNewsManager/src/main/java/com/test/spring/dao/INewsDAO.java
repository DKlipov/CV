package com.test.spring.dao;

import com.test.spring.model.Category;
import com.test.spring.model.News;
import com.test.spring.model.SearchParams;

import java.util.Collection;
import java.util.List;

public interface INewsDAO {
    void addNew(News n);

    void updateNew(News n);

    Collection<News> listNews();

    News getNewsById(int id);

    void removeNews(int id);

    Collection<News> getNewsByCategory(int categoryId);

    List<Category> listNewsCategories();

    Collection<News> searchNews(SearchParams params);
}
