package com.searcher.service;

import com.searcher.entity.SearchData;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import java.util.List;

import static com.searcher.util.Constants.*;

@Service
public class SearchService {

    public List<SearchData> getSearchDataPerPage(List<SearchData> dataList, int offset, int noOfRecords) {
        int range = offset + noOfRecords;
        if (range > dataList.size()) {
            range = dataList.size();
        }
        return dataList.subList(offset, range);
    }

    public int getCorrectOffset(int dataSize, int currentOffset, int recordsPerPage) {
        int range = currentOffset + recordsPerPage;
        boolean isCorrectShift = dataSize >= range;
        if (!isCorrectShift) {
            currentOffset = dataSize - recordsPerPage;
            currentOffset = currentOffset < ZERO ? ZERO : currentOffset;
        }
        return currentOffset;
    }

    public void addDataToModel(List<SearchData> searchDataList, Integer page, Model model) {
        if (searchDataList != null && searchDataList.size() > ZERO) {
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
}
