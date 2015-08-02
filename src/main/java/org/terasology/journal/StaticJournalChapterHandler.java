/*
 * Copyright 2014 MovingBlocks
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

import org.terasology.journal.part.TextJournalPart;
import org.terasology.journal.part.TimestampJournalPart;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.HorizontalAlign;
import org.terasology.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.flow.ContainerRenderSpace;
import org.terasology.rendering.nui.widgets.browser.ui.ParagraphRenderable;
import org.terasology.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Deprecated
public class StaticJournalChapterHandler implements JournalChapterHandler {
    private Map<String, JournalEntryProducer> journalEntries = new HashMap<>();

    public void registerJournalEntry(String entryId, boolean timestamp, String text) {
        if (timestamp) {
            journalEntries.put(entryId, new EntryPartListProducer(Arrays.asList(new TimestampJournalPart(), new TextJournalPart(text, HorizontalAlign.LEFT))));
        } else {
            journalEntries.put(entryId, new EntryPartListProducer(Arrays.asList(new TextJournalPart(text, HorizontalAlign.LEFT))));
        }
    }

    public void registerJournalEntry(String entryId, List<JournalManager.JournalEntryPart> journalEntryParts) {
        journalEntries.put(entryId, new EntryPartListProducer(journalEntryParts));
    }

    @Override
    public Collection<ParagraphData> resolveJournalEntryParts(String entryId, long date) {
        return journalEntries.get(entryId).produceParagraph(date);
    }

    private final class EntryPartListProducer implements JournalEntryProducer {
        private List<JournalManager.JournalEntryPart> journalEntryParts;

        private EntryPartListProducer(List<JournalManager.JournalEntryPart> journalEntryParts) {
            this.journalEntryParts = journalEntryParts;
        }

        @Override
        public List<ParagraphData> produceParagraph(long date) {
            List<ParagraphData> result = new LinkedList<>();
            for (JournalManager.JournalEntryPart journalEntryPart : journalEntryParts) {
                result.add(new ParagraphDataAdapter(journalEntryPart, date));
            }

            return result;
        }
    }

    private final class ParagraphDataAdapter implements ParagraphData {
        private JournalManager.JournalEntryPart journalEntryPart;
        private long date;

        private ParagraphDataAdapter(JournalManager.JournalEntryPart journalEntryPart, long date) {
            this.journalEntryPart = journalEntryPart;
            this.date = date;
        }

        @Override
        public ParagraphRenderStyle getParagraphRenderStyle() {
            return null;
        }

        @Override
        public ParagraphRenderable getParagraphContents() {
            return new ParagraphRenderable() {
                @Override
                public void renderContents(Canvas canvas, Vector2i startPos, ContainerRenderSpace containerRenderSpace, int leftIndent, int rightIndent,
                                           ParagraphRenderStyle defaultStyle, HorizontalAlign horizontalAlign, HyperlinkRegister hyperlinkRegister) {
                    int width = containerRenderSpace.getWidthForVerticalPosition(startPos.getY());
                    int preferredHeight = getPreferredContentsHeight(defaultStyle, startPos.y, containerRenderSpace, leftIndent + rightIndent);
                    journalEntryPart.render(canvas, Rect2i.createFromMinAndSize(startPos.x + leftIndent, startPos.y, width, preferredHeight), date);
                }

                @Override
                public int getPreferredContentsHeight(ParagraphRenderStyle defaultStyle, int yStart, ContainerRenderSpace containerRenderSpace, int sideIndents) {
                    return journalEntryPart.getPreferredSize(date).y;
                }

                @Override
                public int getContentsMinWidth(ParagraphRenderStyle defaultStyle) {
                    return journalEntryPart.getPreferredSize(date).x;
                }
            };
        }
    }
}
