package com.searcher.service;

import com.searcher.entity.Page;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

import static com.searcher.util.Constants.*;

@Service
public class RecordService {

    @Autowired
    private Analyzer analyzer;
    @Autowired
    private Directory directory;

    public void recordPagesToDirectory(Set<Page> pages) {
        if (pages != null && pages.size() > 0) {
            IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, analyzer);
            try (IndexWriter w = new IndexWriter(directory, config)) {
                for (Page page : pages) {
                    addDataToDoc(w, page);
                }
            } catch (IOException e) {
                System.out.println("Record error: " + e.getMessage());
            }
            pages.clear();
        }
    }

    private void addDataToDoc(IndexWriter writer, Page page) throws IOException {
        Document document = new Document();
        document.add(new TextField(TEXT, page.getAllText(), Field.Store.YES));
        document.add(new TextField(HEIGHT_SCORE_TEXT, page.getHeightScoreText(), Field.Store.NO));
        document.add(new StoredField(URL, page.getUrl()));
        document.add(new StoredField(TITLE, page.getTitle()));
        writer.addDocument(document);
    }
}
