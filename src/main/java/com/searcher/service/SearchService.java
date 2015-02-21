package com.searcher.service;

import com.searcher.component.Searcher;
import com.searcher.dao.PageDao;
import com.searcher.entity.Page;
import com.searcher.entity.SearchData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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
