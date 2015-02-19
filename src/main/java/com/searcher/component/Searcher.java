package com.searcher.component;

import com.searcher.entity.Page;
import com.searcher.entity.SearchData;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.searcher.util.Constants.*;

@Component
public class Searcher {

    private Set<Page> pages = Collections.newSetFromMap(new ConcurrentHashMap<Page, Boolean>());

    public List<SearchData> searchWord(String word) throws IOException, InterruptedException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory directory = recordPagesToDirectory(analyzer, pages);
        Query q = null;
        try {
            MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{TEXT, HEIGHT_SCORE_TEXT}, analyzer);
            q = parser.parse(word);
        } catch (ParseException e) {
            System.out.println("Parse error " + e.getMessage());
        }
        if (q != null) {
            return findSimilarities(directory, q);
        }
        throw new IOException("Query is null");
    }

    private Directory recordPagesToDirectory(Analyzer analyzer, Set<Page> pages) {
        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
        try (IndexWriter w = new IndexWriter(directory, config)) {
            for (Page page : pages) {
                addDataToDoc(w, page);
            }
        } catch (IOException e) {
            System.out.println("Record error: " + e.getMessage());
        }
        return directory;
    }

    private void addDataToDoc(IndexWriter writer, Page page) throws IOException {
        Document document = new Document();
        document.add(new TextField(TEXT, page.getAllText(), Field.Store.YES));
        document.add(new TextField(HEIGHT_SCORE_TEXT, page.getHeightScoreText(), Field.Store.YES));
        document.add(new StoredField(URL, page.getUrl()));
        document.add(new StoredField(TITLE, page.getTitle()));
        writer.addDocument(document);
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

        return new SearchData(url, title, score);
    }

    public Set<Page> getPages() {
        return pages;
    }

    public void setPages(Set<Page> pages) {
        this.pages = pages;
    }
}
