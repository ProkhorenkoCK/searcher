package com.searcher.service;

import com.searcher.entity.Page;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.searcher.util.Constants.*;

@Service
public class RecordService {

    @Autowired
    private IndexWriter writer;

    public void recordPageToDirectory(Page page) {
        if (page != null) {
            try {
                addDataToDoc(writer, page);
            } catch (IOException e) {
                System.out.println("Record error: " + e.getMessage());
            }
        }
    }

    private void addDataToDoc(IndexWriter writer, Page page) throws IOException {
        Document document = new Document();
        document.add(new TextField(TEXT, page.getAllText(), Field.Store.YES));
        document.add(new TextField(HEIGHT_SCORE_TEXT, page.getHeightScoreText(), Field.Store.NO));
        document.add(new StoredField(URL, page.getUrl()));
        document.add(new StoredField(TITLE, page.getTitle()));
        writer.addDocument(document);
        writer.commit();
    }
}
