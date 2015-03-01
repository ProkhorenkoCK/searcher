package com.searcher.component;

import com.searcher.entity.SearchData;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;

import static com.searcher.util.Constants.*;

@Component
public class Searcher {

    @Autowired
    private Directory directory;
    @Autowired
    private MultiFieldQueryParser parser;

    public List<SearchData> searchWord(String word) {
        try {
            Query q = parser.parse(word);
            return findSimilarities(directory, q);
        } catch (ParseException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    private List<SearchData> findSimilarities(Directory directory, Query q) {
        List<SearchData> searchDataList = new ArrayList<>();
        int hitsPerPage = HITS_PER_PAGE;
        try (IndexReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
            searcher.search(q, collector);

            ScoreDoc[] hits = collector.topDocs().scoreDocs;
            for (ScoreDoc scoreDoc : hits) {
                searchDataList.add(createSearchBean(searcher, scoreDoc));
            }
        } catch (IOException e) {
            System.out.println("Search error: " + e.getMessage());
        }
        return searchDataList;
    }

    private SearchData createSearchBean(IndexSearcher searcher, ScoreDoc scoreDoc) throws IOException {
        int docId = scoreDoc.doc;
        float score = scoreDoc.score;
        Document doc = searcher.doc(docId);

        String url = doc.getField(URL).stringValue();
        String title = doc.getField(TITLE).stringValue();
        String text = doc.getField(TITLE).stringValue();
        int textLength = text.length() > TEXT_LENGTH ? TEXT_LENGTH : text.length();
        text = text.substring(ZERO, textLength);
        return new SearchData(url, title, score, text);
    }
}
