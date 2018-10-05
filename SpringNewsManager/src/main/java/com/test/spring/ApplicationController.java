package com.test.spring;

import com.test.spring.model.Category;
import com.test.spring.model.News;
import com.test.spring.model.SearchParams;
import com.test.spring.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ApplicationController {

    private WebService webService;

    @Autowired
    @Qualifier(value = "webService")
    public void setWebService(WebService ps) {
        this.webService = ps;
    }

    @RequestMapping(value = "/categories", method = RequestMethod.GET)
    public String listCategories(Model model) {
        List<Category> list = webService.listNewsCategories();
        list.add(0, new Category("все"));
        model.addAttribute("listCategories", list);
        return "categories";
    }

    @RequestMapping(value = "/category/{id}")
    public String newsByCat(@PathVariable("id") int id, Model model) {
        model.addAttribute("listNews", this.webService.getNewsByCategory(id));
        model.addAttribute("pageTitle", "Результаты поиска по категории:");
        model.addAttribute("search", new SearchParams());
        return "newsList";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String listNews(Model model) {
        model.addAttribute("listNews", this.webService.listNews());
        model.addAttribute("search", new SearchParams());
        return "newsList";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute("editableNew", new News());
        model.addAttribute("categoriesList", webService.listNewsCategories());
        return "addEdit";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String edit(@ModelAttribute("editableNew") News n, @RequestParam("data") String data) {
        n.getCategory().setName(data);
        if (n.getId() == 0) {
            this.webService.addNew(n);
        } else {
            this.webService.updateNew(n);
        }
        return "redirect:/";
    }

    @RequestMapping("/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        model.addAttribute("editableNew", webService.getNewsById(id));
        model.addAttribute("categoriesList", webService.listNewsCategories());
        return "addEdit";
    }

    @RequestMapping("/remove/{id}")
    public String remove(@PathVariable("id") int id) {

        this.webService.removeNews(id);
        return "redirect:/";
    }

    @RequestMapping("/search")
    public String search(@ModelAttribute("search") SearchParams params, Model model) {
        model.addAttribute("listNews", this.webService.searchNews(params));
        model.addAttribute("pageTitle", "Результаты поиска:");
        return "newsList";
    }

    @RequestMapping("/fullSearch")
    public String fullSearch(Model model) {
        model.addAttribute("listNews", this.webService.listNews());
        model.addAttribute("search", SearchParams.fullSearch());
        return "newsList";
    }

}
