package com.searcher.app;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.context.annotation.*;

import java.io.IOException;

import static com.searcher.util.Constants.HEIGHT_SCORE_TEXT;
import static com.searcher.util.Constants.TEXT;

@ComponentScan(basePackages = "com.searcher")
@Configuration
public class AppConfiguration {

    @Bean(name="analyzer")
    public Analyzer getAnalyzer() {
        return new StandardAnalyzer();
    }

    @Bean(name = "directory")
    public Directory getDirectory() {
        return new RAMDirectory();
    }

    @Bean(name = "parser")
    public MultiFieldQueryParser getQueryParser() {
        return new MultiFieldQueryParser(new String[]{TEXT, HEIGHT_SCORE_TEXT}, getAnalyzer());
    }

    @Bean(name = "indexWriterConfig")
    public IndexWriterConfig getIndexWriterConfig() {
        return new IndexWriterConfig(Version.LATEST, getAnalyzer());
    }

    @Bean(name = "indexWriter")
    public IndexWriter getIndexWriter() throws IOException {
        return new IndexWriter(getDirectory(), getIndexWriterConfig());
    }
}
