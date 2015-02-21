package com.searcher.util;


import com.searcher.entity.Page;
import com.searcher.util.Constants;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.searcher.util.Constants.*;

public class PageParser {

    /**
     * Method adds height score text and plain text to page
     *
     * @param page
     * @return
     */
    public Page updateInstancePage(Page page) {
        page.setHeightScoreText(getHeightScoreText(page.getBody()).toString());
        page.setAllText(page.getBody().text());
        return page;
    }

    /**
     * Method parses body elements and gets height score text
     *
     * @param body
     * @return
     */
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
