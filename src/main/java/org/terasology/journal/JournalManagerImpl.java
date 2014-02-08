/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.journal;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.ComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.journal.part.TextJournalPart;
import org.terasology.registry.Share;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.nui.HorizontalAlign;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterSystem
@Share(JournalManager.class)
public class JournalManagerImpl implements ComponentSystem, JournalManager {
    private Map<String, JournalChapter> journalChapters = new LinkedHashMap<>();
    private Map<String, Map<String, List<JournalEntryPart>>> journalEntries = new HashMap<>();

    @Override
    public void initialise() {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void registerJournalChapter(String chapterId, Texture icon, String name) {
        journalChapters.put(chapterId, new JournalChapter(icon, name));
    }

    @Override
    public void registerJournalEntry(String chapterId, String entryId, String text) {
        if (!journalChapters.containsKey(chapterId)) {
            throw new IllegalStateException("Unable to add entry to an unknown chapter");
        }
        Map<String, List<JournalEntryPart>> chapterEntries = journalEntries.get(chapterId);
        if (chapterEntries == null) {
            chapterEntries = new LinkedHashMap<>();
            journalEntries.put(chapterId, chapterEntries);
        }
        chapterEntries.put(entryId, Collections.<JournalEntryPart>singletonList(new TextJournalPart(text, HorizontalAlign.LEFT)));
    }

    @Override
    public void registerJournalEntry(String chapterId, String entryId, List<JournalEntryPart> journalEntryParts) {
        if (!journalChapters.containsKey(chapterId)) {
            throw new IllegalStateException("Unable to add entry to an unknown chapter");
        }
        Map<String, List<JournalEntryPart>> chapterEntries = journalEntries.get(chapterId);
        if (chapterEntries == null) {
            chapterEntries = new LinkedHashMap<>();
            journalEntries.put(chapterId, chapterEntries);
        }
        chapterEntries.put(entryId, journalEntryParts);
    }

    @Override
    public boolean hasEntry(EntityRef player, String chapterId, String entryId) {
        JournalAccessComponent journal = player.getComponent(JournalAccessComponent.class);
        List<String> entryIds = journal.discoveredJournalEntries.get(chapterId);
        if (entryIds == null) {
            return false;
        }
        for (String id : entryIds) {
            String[] entrySplit = id.split("\\|", 2);
            if (entrySplit[1].equals(entryId)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Map<JournalManager.JournalChapter, List<JournalManager.JournalEntry>> getPlayerEntries(EntityRef player) {
        Map<JournalManager.JournalChapter, List<JournalManager.JournalEntry>> result = new LinkedHashMap<>();
        for (Map.Entry<String, JournalChapter> chapterEntry : journalChapters.entrySet()) {
            JournalAccessComponent journal = player.getComponent(JournalAccessComponent.class);
            Map<String, List<String>> discoveredEntries = journal.discoveredJournalEntries;
            String chapterId = chapterEntry.getKey();
            List<String> discoveredChapterEntries = discoveredEntries.get(chapterId);
            if (discoveredChapterEntries != null) {
                List<JournalManager.JournalEntry> chapterEntries = new LinkedList<>();

                for (String discoveredChapterEntryId : discoveredChapterEntries) {
                    String[] entrySplit = discoveredChapterEntryId.split("\\|", 2);
                    long date = Long.parseLong(entrySplit[0]);
                    String id = entrySplit[1];
                    chapterEntries.add(new JournalEntry(date, journalEntries.get(chapterId).get(id)));
                }

                result.put(chapterEntry.getValue(), chapterEntries);
            }
        }

        return result;
    }

    private final class JournalEntry implements JournalManager.JournalEntry {
        private final long date;
        private final List<JournalEntryPart> parts;

        private JournalEntry(long date, List<JournalEntryPart> parts) {
            this.date = date;
            this.parts = parts;
        }

        @Override
        public long getDate() {
            return date;
        }

        @Override
        public List<JournalEntryPart> getParts() {
            return parts;
        }
    }

    private final class JournalChapter implements JournalManager.JournalChapter {
        private final Texture texture;
        private final String name;

        private JournalChapter(Texture texture, String name) {
            this.texture = texture;
            this.name = name;
        }

        @Override
        public String getChapterName() {
            return name;
        }

        @Override
        public Texture getTexture() {
            return texture;
        }
    }
}
