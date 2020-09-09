// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.Share;
import org.terasology.engine.rendering.assets.texture.TextureRegion;
import org.terasology.engine.rendering.nui.widgets.browser.data.DocumentData;

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
    private final Map<String, JournalChapter> journalChapters = new LinkedHashMap<>();
    private final Map<String, JournalChapterHandler> journalChapterHandlers = new HashMap<>();

    @Override
    public void registerJournalChapter(String chapterId, TextureRegion icon, String name,
                                       JournalChapterHandler browserJournalChapterHandler) {
        journalChapters.put(chapterId, new JournalChapter(icon, name, chapterId));
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
            String[] entrySplit = id.split("\\|", 3);
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
                    String[] entrySplit = discoveredChapterEntryId.split("\\|", 3);
                    long date = Long.parseLong(entrySplit[0]);
                    String id = entrySplit[1];
                    String status = entrySplit[2];
                    if (status.equals("read")) {
                        chapterEntries.addReadParagraphs(journalChapterHandler.resolveJournalEntryParts(id, date));
                    } else {
                        chapterEntries.addUnreadParagraphs(journalChapterHandler.resolveJournalEntryParts(id, date));
                    }
                }

                result.put(chapterEntry.getValue(), chapterEntries);
            }
        }

        return result;
    }

    private final class JournalChapter implements JournalManager.JournalChapter {
        private final TextureRegion texture;
        private final String name;
        private final String chapterId;

        private JournalChapter(TextureRegion texture, String name, String chapterId) {
            this.texture = texture;
            this.name = name;
            this.chapterId = chapterId;
        }

        @Override
        public String getChapterName() {
            return name;
        }

        @Override
        public String getChapterId() {
            return chapterId;
        }

        @Override
        public TextureRegion getTexture() {
            return texture;
        }
    }
}
