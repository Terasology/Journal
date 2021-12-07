// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.engine.rendering.nui.widgets.browser.data.basic.HTMLLikeParser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BrowserJournalChapterHandler implements JournalChapterHandler {
    private Map<String, JournalEntryProducer> entryMap = new HashMap<>();

    public void registerJournalEntry(String entryId, String htmlLikeText) {
        entryMap.put(entryId, new StaticJournalEntryProducer(Collections.singleton(HTMLLikeParser.parseHTMLLikeParagraph(null, htmlLikeText))));
    }

    public void registerJournalEntry(String entryId, Collection<ParagraphData> paragraphs) {
        entryMap.put(entryId, new StaticJournalEntryProducer(paragraphs));
    }

    public void registerJournalEntry(String entryId, JournalEntryProducer journalEntryProducer) {
        entryMap.put(entryId, journalEntryProducer);
    }

    @Override
    public Collection<ParagraphData> resolveJournalEntryParts(String entryId, long date) {
        return entryMap.get(entryId).produceParagraph(date);
    }

    private static final class StaticJournalEntryProducer implements JournalEntryProducer {
        private Collection<ParagraphData> paragraphs;

        private StaticJournalEntryProducer(Collection<ParagraphData> paragraphs) {
            this.paragraphs = paragraphs;
        }

        @Override
        public Collection<ParagraphData> produceParagraph(long date) {
            return paragraphs;
        }
    }
}
