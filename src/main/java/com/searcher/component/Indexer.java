package com.searcher.component;

import com.searcher.app.IndexTask;
import com.searcher.entity.Page;
import com.searcher.service.PageService;
import com.searcher.service.RecordService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static com.searcher.util.Constants.*;

@Component
public class Indexer {

    @Autowired
    private PageService pageService;
    @Autowired
    private RecordService recordService;

    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public void indexPage(String url, int depth) {
        Set<String> indexedLinks = new HashSet<>();
        indexedLinks.add(url);
        recursiveIndex(url, depth, indexedLinks);
    }

    public void recursiveIndex(String url, int depth, Set<String> indexedLinks) {
        if (depth < ZERO) return;
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
        Page page = pageService.createPageBean(document);
        recordService.recordPageToDirectory(page);
        for (String link : getLinks(document)) {
            boolean isNotIndexedLink = indexedLinks.add(link);
            if (isNotIndexedLink) {
                executorService.execute(new IndexTask(this, link, depth - 1, indexedLinks));
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
