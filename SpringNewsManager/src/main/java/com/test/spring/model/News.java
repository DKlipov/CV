package com.test.spring.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="NEWS")
public class News {

    public News(){
        category=new Category();
    }

    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)
    private Category category;


    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "NEWSDATE")
    private Date date;



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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString(){
        return "id="+id+", name="+name;
    }
}
