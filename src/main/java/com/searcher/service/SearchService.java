package com.searcher.service;

import com.searcher.component.Searcher;
import com.searcher.dao.PageDao;
import com.searcher.entity.Page;
import com.searcher.entity.SearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static com.searcher.util.Constants.LAST_QUERY;
import static com.searcher.util.Constants.SEARCH_DATA;

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

    private void addSearchDataAndQueryToSession(HttpSession session, List<SearchData> searchDataList, String query) {
        session.setAttribute(SEARCH_DATA, searchDataList);
        session.setAttribute(LAST_QUERY, query);
    }
}
