package com.test.spring.service;

import java.util.Collection;
import java.util.List;

import com.test.spring.model.Category;
import com.test.spring.model.News;
import com.test.spring.model.SearchParams;

public interface WebService {
	void addNew(News n);
	void updateNew(News n);
	Collection<News> listNews();
	News getNewsById(int id);
	void removeNews(int id);
	Collection<News> getNewsByCategory(int categoryId);
	List<Category> listNewsCategories();
	Collection<News> searchNews(SearchParams params);
}
