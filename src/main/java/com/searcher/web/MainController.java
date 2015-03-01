package com.searcher.web;

import com.searcher.app.Indexer;
import com.searcher.component.Searcher;
import com.searcher.entity.SearchData;
import com.searcher.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.searcher.util.Constants.*;

@Controller
@RequestMapping(value = "/")
public class MainController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private Searcher searcher;
    @Autowired
    private Indexer indexer;

    @RequestMapping(method = RequestMethod.GET)
    public String getStartPage() {
        return "startPage";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String getIndexPage() {
        return "index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String index(@RequestParam("url") String url,
                        @RequestParam("depth") int depth) throws IOException {
        if (depth >= ZERO) {
            indexer.indexPage(url, depth);
        } else {
            System.out.println("WARN: depth is less zero " + depth);
        }
        return "redirect:/search";
    }

    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    @SuppressWarnings("unchecked")
    public String search(@RequestParam(value = "q", required = false) String query,
                         @RequestParam(value = "page", required = false) Integer page,
                         Model model,
                         HttpSession session) {
        if (query != null && query.trim().length() > 0) {
            String lastQuery = (String) session.getAttribute(LAST_QUERY);
            List<SearchData> searchDataList = getSearchDataDependsLastQuery(session, lastQuery, query);
            searchService.addDataToModel(searchDataList, page, model);
        } else {
            clearDataInSession(session);
        }
        return "search";
    }

    @SuppressWarnings("unchecked")
    private void clearDataInSession(HttpSession session) {
        List<SearchData> searchDataList = (List<SearchData>) session.getAttribute(SEARCH_DATA);
        if (searchDataList != null) {
            session.removeAttribute(SEARCH_DATA);
        }
    }

    private void addSearchDataAndQueryToSession(HttpSession session, List<SearchData> searchDataList, String query) {
        session.setAttribute(SEARCH_DATA, searchDataList);
        session.setAttribute(LAST_QUERY, query);
    }

    @SuppressWarnings("unchecked")
    private List<SearchData> getSearchDataDependsLastQuery(HttpSession session, String lastQuery, String query) {
        List<SearchData> searchDataList;
        if (lastQuery != null && lastQuery.equals(query)) {
            searchDataList = (List<SearchData>) session.getAttribute(SEARCH_DATA);
        } else {
            searchDataList = findSearchData(query);
            addSearchDataAndQueryToSession(session, searchDataList, query);
        }
        return searchDataList;
    }

    private List<SearchData> findSearchData(String query) {
        List<SearchData> searchDataList;
        try {
            searchDataList = searcher.searchWord(query);
        } catch (IllegalStateException e) {
            System.out.println("Wrong query: " + e.getMessage());
            return new ArrayList<>();
        }
        return searchDataList;
    }
}
