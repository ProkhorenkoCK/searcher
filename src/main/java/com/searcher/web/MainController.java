package com.searcher.web;

import com.searcher.app.IndexCrawler;
import com.searcher.component.Searcher;
import com.searcher.dao.PageDao;
import com.searcher.entity.SearchData;
import com.searcher.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    private SearchService searchService;
    @Autowired
    private Searcher searcher;

    @RequestMapping(method = RequestMethod.GET)
    public String getSearchPage() {
        return "startPage";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndexPage() {
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String search(@RequestParam("url") String url,
                         @RequestParam("depth") int depth) throws IOException {
        IndexCrawler crawler = new IndexCrawler();
        try {
            if (depth >= ZERO) {
                crawler.indexPage(url, depth, searchService.getPages());
            } else {
                System.out.println("WARN: depth is less zero " + depth);
            }
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "redirect:/search";
    }

    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    @SuppressWarnings("unchecked")
    public String searchData(@RequestParam(value = "q", required = false) String query,
                             @RequestParam(value = "page", required = false) Integer page,
                             Model model,
                             HttpSession session) {
        try {
            if (query != null && query.trim().length() > 0) {
                List<SearchData> searchDataList;
                String lastQuery = (String)session.getAttribute(LAST_QUERY);
                if (lastQuery == null) {
                    searchDataList = searcher.searchWord(query, searchService.getPages());
                    addSearchDataAndQueryToSession(session, searchDataList, query);
                } else {
                    if (lastQuery.equals(query)) {
                        searchDataList = (List<SearchData>)session.getAttribute(SEARCH_DATA);
                    } else {
                        searchDataList = searcher.searchWord(query, searchService.getPages());
                        addSearchDataAndQueryToSession(session, searchDataList, query);
                    }
                }
                if (searchDataList != null && searchDataList.size() > ZERO) {
                   searchService.addDataToModel(searchDataList, page, model);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getClass() + " " + e.getMessage());
        }
        return "search";
    }

    private void addSearchDataAndQueryToSession(HttpSession session, List<SearchData> searchDataList, String query) {
        session.setAttribute(SEARCH_DATA, searchDataList);
        session.setAttribute(LAST_QUERY, query);
    }
}
