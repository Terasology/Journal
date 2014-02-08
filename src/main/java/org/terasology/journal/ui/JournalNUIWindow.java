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
package org.terasology.journal.ui;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.journal.JournalManager;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.nui.CoreScreenLayer;
import org.terasology.rendering.nui.databinding.Binding;
import org.terasology.rendering.nui.layouts.ScrollableArea;
import org.terasology.rendering.nui.widgets.UIList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class JournalNUIWindow extends CoreScreenLayer {
    private UIList<JournalManager.JournalEntry> journalList;
    private JournalEntryRenderer renderer = new JournalEntryRenderer();

    @Override
    public void initialise() {
        final ScrollableArea scrollArea = find("scrollable", ScrollableArea.class);
        scrollArea.moveToBottom();

        journalList = (UIList<JournalManager.JournalEntry>) find("journalList", UIList.class);

        journalList.setItemRenderer(renderer);
        journalList.bindSelection(
                new Binding<JournalManager.JournalEntry>() {
                    @Override
                    public JournalManager.JournalEntry get() {
                        return null;
                    }

                    @Override
                    public void set(JournalManager.JournalEntry value) {
                    }
                });

        updateJournal();
    }

    private void updateJournal() {
        JournalManager journalManager = CoreRegistry.get(JournalManager.class);
        EntityRef playerEntity = CoreRegistry.get(LocalPlayer.class).getCharacterEntity();
        Map<JournalManager.JournalChapter, List<JournalManager.JournalEntry>> playerEntries = journalManager.getPlayerEntries(playerEntity);

        List<JournalManager.JournalEntry> entries = new LinkedList<>();

        for (List<JournalManager.JournalEntry> chapterContents : playerEntries.values()) {
            for (JournalManager.JournalEntry chapterEntry : chapterContents) {
                entries.add(chapterEntry);
            }
        }

        journalList.setList(entries);
    }
}
