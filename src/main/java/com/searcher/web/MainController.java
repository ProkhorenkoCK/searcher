package com.searcher.web;

import com.searcher.app.IndexCrawler;
import com.searcher.component.Searcher;
import com.searcher.entity.SearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

import static com.searcher.util.Constants.*;

@Controller
@RequestMapping(value = "/")
public class MainController {

    @Autowired
    private Searcher searcher;

    @RequestMapping(method = RequestMethod.GET)
    public String getSearchPage(HttpSession session) {

        return "startPage";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndexPage(HttpServletRequest request, HttpSession session) {

        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String search(@RequestParam("q") String url,
                         @RequestParam("depth") int depth) throws IOException {
        IndexCrawler crawler = new IndexCrawler();
        try {
            if (depth >= ZERO) {
                crawler.indexPage(url, depth, searcher.getPages());
            } else {
                System.out.println("WARN: depth is less zero " + depth);
            }
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "search";
    }

    @RequestMapping(value = "/search",  method = { RequestMethod.GET, RequestMethod.POST })
    public String searchData(@RequestParam(value = "q", required = false) String query, HttpSession session) throws IOException, InterruptedException {
        List<SearchData> dataList = searcher.searchWord(query);
        session.setAttribute(SESSION_SEARCH_DATA, dataList.toString());
        return "search";
    }
}
