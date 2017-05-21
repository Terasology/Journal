/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.journal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.rendering.nui.Color;
import org.terasology.rendering.nui.HorizontalAlign;
import org.terasology.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.html.basic.DefaultParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.html.basic.ParagraphBuilder;
import org.terasology.rendering.nui.widgets.browser.ui.style.DocumentRenderStyle;
import org.terasology.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DefaultDocumentData implements DocumentData {
    private DocumentRenderStyle documentRenderStyle;
    private List<DefaultParagraphData> paragraphs = new LinkedList<>();
    private static final Logger logger = LoggerFactory.getLogger(DefaultDocumentData.class);

    public DefaultDocumentData(DocumentRenderStyle documentRenderStyle) {
        this.documentRenderStyle = documentRenderStyle;
    }


    private ParagraphRenderStyle unreadParagraph = new ParagraphRenderStyle() {
        @Override
        public Color getParagraphBackground() {
            return Color.GREY;
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
