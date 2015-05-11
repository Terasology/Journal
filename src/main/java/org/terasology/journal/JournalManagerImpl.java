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
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.Share;
import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.widgets.browser.data.DocumentData;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterSystem
@Share(JournalManager.class)
public class JournalManagerImpl extends BaseComponentSystem implements JournalManager {
    private Map<String, JournalChapter> journalChapters = new LinkedHashMap<>();
    private Map<String, JournalChapterHandler> journalChapterHandlers = new HashMap<>();

    @Override
    public void registerJournalChapter(String chapterId, TextureRegion icon, String name, JournalChapterHandler browserJournalChapterHandler) {
        journalChapters.put(chapterId, new JournalChapter(icon, name));
        journalChapterHandlers.put(chapterId, browserJournalChapterHandler);
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
    public Map<JournalManager.JournalChapter, DocumentData> getPlayerEntries(EntityRef player) {
        Map<JournalManager.JournalChapter, DocumentData> result = new LinkedHashMap<>();
        for (Map.Entry<String, JournalChapter> chapterEntry : journalChapters.entrySet()) {
            JournalAccessComponent journal = player.getComponent(JournalAccessComponent.class);
            Map<String, List<String>> discoveredEntries = journal.discoveredJournalEntries;
            String chapterId = chapterEntry.getKey();
            List<String> discoveredChapterEntries = discoveredEntries.get(chapterId);
            if (discoveredChapterEntries != null) {
                DefaultDocumentData chapterEntries = new DefaultDocumentData(null);

                JournalChapterHandler journalChapterHandler = journalChapterHandlers.get(chapterId);
                for (String discoveredChapterEntryId : discoveredChapterEntries) {
                    String[] entrySplit = discoveredChapterEntryId.split("\\|", 2);
                    long date = Long.parseLong(entrySplit[0]);
                    String id = entrySplit[1];
                    chapterEntries.addParagraphs(journalChapterHandler.resolveJournalEntryParts(id, date));
                }

                result.put(chapterEntry.getValue(), chapterEntries);
            }
        }

        return result;
    }

    private final class JournalChapter implements JournalManager.JournalChapter {
        private final TextureRegion texture;
        private final String name;

        private JournalChapter(TextureRegion texture, String name) {
            this.texture = texture;
            this.name = name;
        }

        @Override
        public String getChapterName() {
            return name;
        }

        @Override
        public TextureRegion getTexture() {
            return texture;
        }
    }
}
