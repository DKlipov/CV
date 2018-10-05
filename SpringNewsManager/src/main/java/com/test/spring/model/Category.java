package com.test.spring.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="CATEGORY")
public class Category {


    public Category(){
    }
    public Category(String s){
        name=s;
    }

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;


    private String name;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(fetch = FetchType.EAGER, mappedBy="category")
    @ElementCollection(targetClass=News.class)
    private Set<News> news;

    public void setNews(Set<News> news) {
        this.news = news;
    }

    public Set<News> getNews() {
        return this.news;
    }

    @Override
    public String toString(){
        return "id="+id+", name="+name;
    }
}
