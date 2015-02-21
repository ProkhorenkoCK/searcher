package com.searcher.app;

import com.searcher.entity.Page;
import com.searcher.util.PageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static com.searcher.util.Constants.*;

public class IndexCrawler {

    private PageParser pageParser = new PageParser();
    private ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public void indexPage(String url, int depth, ConcurrentHashMap<String, Page> pages) throws IOException, InterruptedException {
        Set<String> indexedLink = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
        recursiveIndex(url, depth, pages, indexedLink);
    }

    public void recursiveIndex(String url, int depth, ConcurrentHashMap<String, Page> pages, Set<String> indexedLink) throws InterruptedException {
        if (depth < ZERO) return;
        if (!indexedLink.add(url)) return;
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
        pages.put(url, updateFieldOfPage(new Page(document)));
        for (String link : getLinks(document)) {
            IndexTask task = new IndexTask(this, link, depth - 1, pages, indexedLink);
            executorService.execute(task);
        }
    }

    private Set<String> getLinks(Document document) {
        Set<String> links = new HashSet<>();
        int countLink = ZERO;
        for (Element linkElement : document.select("a")) {
            if (countLink == MAX_LINKS_PER_PAGE) {
                return links;
            }
            String link = linkElement.absUrl("href");
            if (!link.contains("#") && link.length() > 0) {
                countLink++;
                links.add(link);
            }
        }
        return links;
    }

    private Page updateFieldOfPage(Page page) {
        return pageParser.updateInstancePage(page);
    }
}
