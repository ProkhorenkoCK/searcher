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
    private PageDao pageDao;
    @Autowired
    private SearchService searchService;

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
                crawler.indexPage(url, depth, pageDao.getPages());
            } else {
                System.out.println("WARN: depth is less zero " + depth);
            }
        } catch (InterruptedException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "search";
    }

    @RequestMapping(value = "/search",  method = { RequestMethod.GET, RequestMethod.POST })
    @SuppressWarnings("unchecked")
    public String searchData(@RequestParam(value = "q", required = false) String query,
                             @RequestParam(value = "page", required = false) Integer page,
                             Model model,
                             HttpSession session) {
        try {
            if (query != null) {
                List<SearchData> searchDataList = searchService.getSearchData(session, query, pageDao.getPages());
                if (searchDataList != null && searchDataList.size() > ZERO) {
                    int currentPage = 1;
                    if (page != null && page > ZERO) {
                        currentPage = page;
                    }
                    int recordsPerPage = RECORDS_PER_PAGE;
                    int offset = (currentPage - 1) * recordsPerPage;
                    offset = searchService.getCorrectOffset(searchDataList.size(), offset, recordsPerPage);
                    int noOfRecords = searchDataList.size();
                    int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

                    List<SearchData> pageData = searchService.getSearchDataPerPage(searchDataList, offset, recordsPerPage);

                    model.addAttribute(SEARCH_DATA, pageData);
                    model.addAttribute(NO_OF_PAGES, noOfPages);
                    model.addAttribute(CURRENT_PAGE, currentPage);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println(e.getClass() + " " + e.getMessage());
        }
        return "search";
    }
}
