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
import org.terasology.rendering.nui.HorizontalAlign;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticJournalChapterHandler implements JournalChapterHandler {
    private Map<String, List<JournalManager.JournalEntryPart>> journalEntries = new HashMap<>();

    public void registerJournalEntry(String entryId, String text) {
        journalEntries.put(entryId, Collections.<JournalManager.JournalEntryPart>singletonList(new TextJournalPart(text, HorizontalAlign.LEFT)));
    }

    public void registerJournalEntry(String entryId, List<JournalManager.JournalEntryPart> journalEntryParts) {
        journalEntries.put(entryId, journalEntryParts);
    }

    @Override
    public List<JournalManager.JournalEntryPart> resolveJournalEntryParts(String entryId) {
        return journalEntries.get(entryId);
    }
}
