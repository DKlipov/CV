package com.test.spring.service;

import java.util.Collection;
import java.util.List;

import com.test.spring.dao.NewsDAO;
import com.test.spring.model.Category;
import com.test.spring.model.News;
import com.test.spring.model.SearchParams;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WebServiceImpl implements WebService {

    private NewsDAO newsDAO;

    public void setNewsDAO(NewsDAO newsDAO) {
        this.newsDAO = newsDAO;
    }

    @Override
    @Transactional
    public void addNew(News n) {
        newsDAO.addNew(n);
    }

    @Override
    @Transactional
    public void updateNew(News n) {
        newsDAO.updateNew(n);
    }

    @Override
    @Transactional
    public List<News> listNews() {
        return newsDAO.listNews();
    }

    @Override
    @Transactional
    public News getNewsById(int id) {
        return newsDAO.getNewsById(id);
    }

    @Override
    @Transactional
    public void removeNews(int id) {
        newsDAO.removeNews(id);
    }

    @Override
    @Transactional
    public Collection<News> getNewsByCategory(int categoryId) {
        return newsDAO.getNewsByCategory(categoryId);
    }

    @Override
    @Transactional
    public List<Category> listNewsCategories() {
        return newsDAO.listNewsCategories();
    }

    @Override
    @Transactional
    public List<News> searchNews(SearchParams params) {
        return newsDAO.searchNews(params);
    }
}
