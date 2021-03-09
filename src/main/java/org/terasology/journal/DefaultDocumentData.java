// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.engine.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.engine.rendering.nui.widgets.browser.data.html.basic.DefaultParagraphData;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.DocumentRenderStyle;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;
import org.terasology.nui.Color;
import org.terasology.nui.HorizontalAlign;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DefaultDocumentData implements DocumentData {
    private DocumentRenderStyle documentRenderStyle;
    private List<DefaultParagraphData> paragraphs = new LinkedList<>();
    private static final Logger logger = LoggerFactory.getLogger(DefaultDocumentData.class);

    private static final Color UNREAD_COLOR = new Color(255,255,225);

    public DefaultDocumentData(DocumentRenderStyle documentRenderStyle) {
        this.documentRenderStyle = documentRenderStyle;
    }


    private ParagraphRenderStyle unreadParagraph = new ParagraphRenderStyle() {
        @Override
        public Color getParagraphBackground() {
            return UNREAD_COLOR;
        }
        @Override
        public HorizontalAlign getHorizontalAlignment() {
            return HorizontalAlign.CENTER;
        }
    };

    private ParagraphRenderStyle readParagraph = new ParagraphRenderStyle() {
        @Override
        public HorizontalAlign getHorizontalAlignment() {
            return HorizontalAlign.CENTER;
        }
    };

    @Override
    public DocumentRenderStyle getDocumentRenderStyle() {
        return documentRenderStyle;
    }

    @Override
    public Collection<ParagraphData> getParagraphs() {
        return Collections.unmodifiableList(paragraphs);
    }

    public void addParagraph(DefaultParagraphData paragraph) {
        paragraphs.add(paragraph);
    }

    public void addUnreadParagraphs(Collection<ParagraphData> paragraphsToAdd) {
        if (paragraphsToAdd != null) {
            for (ParagraphData paragraphData : paragraphsToAdd) {
                DefaultParagraphData defaultParagraphData = new DefaultParagraphData(unreadParagraph, paragraphData.getParagraphContents());
                addParagraph(defaultParagraphData);
            }
        }
    }

    public void addReadParagraphs(Collection<ParagraphData> paragraphsToAdd) {
        if (paragraphsToAdd != null) {
            for (ParagraphData paragraphData : paragraphsToAdd) {
                DefaultParagraphData defaultParagraphData = new DefaultParagraphData(readParagraph, paragraphData.getParagraphContents());
                addParagraph(defaultParagraphData);
            }
        }
    }
}
