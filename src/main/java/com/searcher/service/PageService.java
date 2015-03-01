package com.searcher.service;

import com.searcher.entity.Page;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.searcher.util.Constants.HEIGHT_SCORE_TAGS;

@Service
public class PageService {

    public Page createPageBean(Document document) {
        return updateInstancePage(new Page(document));
    }

    private Page updateInstancePage(Page page) {
        page.setHeightScoreText(getHeightScoreText(page.getBody()).toString());
        page.setAllText(page.getBody().text());
        return page;
    }

    private StringBuilder getHeightScoreText(Element body) {
        StringBuilder sb = new StringBuilder();
        List<Element> bodyElements = body.getAllElements();
        List<Element> heightScoreElements = body.select(HEIGHT_SCORE_TAGS);
        bodyElements.removeAll(heightScoreElements);
        for (Element element : heightScoreElements) {
            sb.append(element.text()).append("\n");
        }
        return sb;
    }
}
