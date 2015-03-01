package com.searcher.app;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.context.annotation.*;

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
}
