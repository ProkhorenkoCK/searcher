package com.searcher.service;

import com.searcher.entity.SearchData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    public List<SearchData> getSearchData(List<SearchData> dataList, int offset, int noOfRecords) {
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
}
