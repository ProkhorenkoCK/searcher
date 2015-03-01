package com.searcher.app;

import com.searcher.entity.Page;
import com.searcher.service.PageService;
import com.searcher.service.RecordService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static com.searcher.util.Constants.*;

@Service
public class Indexer {

    @Autowired
    private PageService pageService;
    @Autowired
    private RecordService recordService;

    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public void indexPage(String url, int depth) {
        Set<String> indexedLinks = new HashSet<>();
        Set<Page> pages = new HashSet<>();
        indexedLinks.add(url);
        executorService.execute(new IndexTask(this, url, depth, pages, indexedLinks));
    }

    public void recursiveIndex(String url, int depth, Set<Page> pages, Set<String> indexedLink) {
        if (depth < ZERO) {
            recordService.recordPagesToDirectory(pages);
            return;
        }
        Document document = null;
        try {
            System.out.println("Indexing link: " + url);
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("Something was wrong - link: " + url);
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Not valid URL: " + url);
            return;
        }
        pages.add(pageService.createPageBean(document));
        for (String link : getLinks(document)) {
            boolean isNotIndexedLink = indexedLink.add(link);
            if (isNotIndexedLink) {
                recursiveIndex(link, depth - 1, pages, indexedLink);
            }
        }
    }

    private Set<String> getLinks(Document document) {
        Set<String> links = new HashSet<>();
        for (Element linkElement : document.select("a")) {
            String link = linkElement.absUrl("href");
            if (link.length() > 0) {
                String correctLink = link.split("\\?|#")[0]; //link without query parameters and hash tag
                links.add(correctLink);
            }
        }
        return links;
    }
}
