package com.test.spring.dao;

import com.test.spring.model.Category;
import com.test.spring.model.News;
import com.test.spring.model.SearchParams;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class NewsDAO implements INewsDAO {

    private static final Logger logger = LoggerFactory.getLogger(NewsDAO.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf) {
        sessionFactory = sf;
    }

    private Category loadCategory(String category) {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Category where name = :paramName");
        query.setParameter("paramName", category.toLowerCase());
        List list = query.list();
        if (list.isEmpty()) {
            Category c = new Category(category.toLowerCase());
            session.persist(c);
            logger.info("Added new category: " + c.getName());
            return c;
        }
        logger.info("Category loaded successful");
        return (Category) list.get(0);
    }

    @Override
    public void addNew(News n) {
        Session session = sessionFactory.getCurrentSession();
        n.setCategory(loadCategory(n.getCategory().getName()));
        n.setDate(new Date());
        session.persist(n);
        logger.info("News saved successfully, details: " + n);
    }

    @Override
    public void updateNew(News n) {
        Session session = sessionFactory.getCurrentSession();
        Category newCat = loadCategory(n.getCategory().getName());
        News oldNew = getNewsById(n.getId());
        Category oldCat = oldNew.getCategory();
        oldNew.setCategory(newCat);
        if (n.getName() != null) {
            oldNew.setName(n.getName());
        }
        if (n.getText() != null) {
            oldNew.setText(n.getText());
        }
        session.update(oldNew);
        if (newCat.getId() != oldCat.getId() && oldCat.getNews().size() <= 1) {
            session.delete(oldCat);
        }
        logger.info("News updated successfully, details: " + n);
    }

    @Override
    public List<News> listNews() {
        Session session = this.sessionFactory.getCurrentSession();
        List<News> newsList = session.createQuery("from News").list();
        for (News n : newsList) {
            logger.info("News List::" + n);
        }
        return newsList;
    }

    @Override
    public News getNewsById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        News p = (News) session.load(News.class, id);
        logger.info("News loaded successfully, details: " + p);
        return p;
    }

    @Override
    public void removeNews(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        News p = (News) session.load(News.class, id);
        if (null != p) {
            Category c = p.getCategory();
            session.delete(p);
            if (c.getNews().size() <= 1) {
                session.delete(c);
            }
        }
        logger.info("News deleted successfully, details: " + p);
    }

    @Override
    public Collection<News> getNewsByCategory(int categoryId) {
        Session session = this.sessionFactory.getCurrentSession();
        Query query = session.createQuery("from Category where id = :catId");
        query.setParameter("catId", categoryId);
        List<Category> list = query.list();
        if (list.isEmpty()) {
            logger.info("News in category not found, returned all news");
            return listNews();
        }
        logger.info("News list by category load successfully");
        return list.get(0).getNews();
    }

    @Override
    public List<Category> listNewsCategories() {
        Session session = this.sessionFactory.getCurrentSession();
        List<Category> list = session.createQuery("from Category").list();
        list.sort(Comparator.comparing(Category::getName));
        for (Category n : list) {
            logger.info("News List::" + n);
        }
        return list;
    }

    @Override
    public List<News> searchNews(SearchParams params) {
        Session session = this.sessionFactory.getCurrentSession();
        if (!params.isFullSearch()) {
            Query query = session.createQuery("from News n where n.name like :searchPar or n.text like :searchPar");
            query.setString("searchPar", "%" + params.getQuickSearch() + "%");
            return query.list();
        } else {
            Query query = session.createQuery("select n from News as n inner join n.category as c where n.name like :searchPar1 and n.text like :searchPar2 and  c.name like :searchPar3");
            query.setString("searchPar1", "%" + params.getTitle() + "%");
            query.setString("searchPar2", "%" + params.getText() + "%");
            query.setString("searchPar3", "%" + params.getCategory() + "%");
            return query.list();
        }
    }
}
