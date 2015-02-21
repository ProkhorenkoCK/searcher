package com.searcher.service;

import com.searcher.component.Searcher;
import com.searcher.dao.PageDao;
import com.searcher.entity.Page;
import com.searcher.entity.SearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.searcher.util.Constants.*;

@Service
public class SearchService {

    @Autowired
    private Searcher searcher;
    @Autowired
    private PageDao pageDao;

    @SuppressWarnings("unchecked")
    public List<SearchData> getSearchData(HttpSession session, String query, ConcurrentHashMap<String, Page> pages) throws IOException, InterruptedException {
        List<SearchData> searchDataList;
        String lastQuery = (String)session.getAttribute(LAST_QUERY);
        if (lastQuery == null) {
            searchDataList = searcher.searchWord(query, pages);
            addSearchDataAndQueryToSession(session, searchDataList, query);
        } else {
            if (lastQuery.equals(query)) {
                searchDataList = (List<SearchData>)session.getAttribute(SEARCH_DATA);
            } else {
                searchDataList = searcher.searchWord(query, pages);
                addSearchDataAndQueryToSession(session, searchDataList, query);
            }
        }
        return searchDataList;
    }

    public List<SearchData> getSearchDataPerPage(List<SearchData> dataList, int offset, int noOfRecords) {
        List<SearchData> searchDataList = new ArrayList<>();
        int range = offset + noOfRecords;
        if (range > dataList.size()) {
            range = dataList.size();
        }
        for (int i = offset; i < range; i++) {
            searchDataList.add(dataList.get(i));
        }
        return searchDataList;
    }

    public int getCorrectOffset(int dataSize, int currentOffset, int recordsPerPage) {
        int range = currentOffset + recordsPerPage;
        boolean isCorrectShift = dataSize >= range;
        if (!isCorrectShift && (dataSize >= recordsPerPage)) {
            currentOffset = dataSize - recordsPerPage;
        }
        return currentOffset;
    }

    public ConcurrentHashMap<String, Page> getPages() {
        return pageDao.getPages();
    }

    private void addSearchDataAndQueryToSession(HttpSession session, List<SearchData> searchDataList, String query) {
        session.setAttribute(SEARCH_DATA, searchDataList);
        session.setAttribute(LAST_QUERY, query);
    }

    public void addDataToModel(List<SearchData> searchDataList, Integer page, Model model) {
        int currentPage = 1;
        if (page != null && page > ZERO) {
            currentPage = page;
        }
        int recordsPerPage = RECORDS_PER_PAGE;
        int offset = (currentPage - 1) * recordsPerPage;
        offset = getCorrectOffset(searchDataList.size(), offset, recordsPerPage);
        int noOfRecords = searchDataList.size();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        List<SearchData> pageData = getSearchDataPerPage(searchDataList, offset, recordsPerPage);

        model.addAttribute(SEARCH_DATA, pageData);
        model.addAttribute(NO_OF_PAGES, noOfPages);
        model.addAttribute(CURRENT_PAGE, currentPage);
    }
}
