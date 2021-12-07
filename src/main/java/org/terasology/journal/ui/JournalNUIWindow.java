// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal.ui;

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.CoreRegistry;
import org.terasology.engine.rendering.nui.CoreScreenLayer;
import org.terasology.engine.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.engine.rendering.nui.widgets.browser.ui.BrowserWidget;
import org.terasology.journal.JournalAccessComponent;
import org.terasology.journal.JournalManager;
import org.terasology.nui.UIWidget;
import org.terasology.nui.layouts.ScrollableArea;
import org.terasology.nui.widgets.ItemActivateEventListener;
import org.terasology.nui.widgets.UIList;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class JournalNUIWindow extends CoreScreenLayer {
    private BrowserWidget journalList;
    private UIList<JournalManager.JournalChapter> chapterList;
    private JournalChapterRenderer chapterRenderer = new JournalChapterRenderer();
    private JournalManager.JournalChapter selectedChapter;

    @Override
    public void initialise() {
        final ScrollableArea scrollArea = find("entries", ScrollableArea.class);
        scrollArea.moveToBottom();

        journalList = find("journalList", BrowserWidget.class);

        chapterList = (UIList<JournalManager.JournalChapter>) find("chapterList", UIList.class);

        chapterList.setItemRenderer(chapterRenderer);
        chapterList.subscribe(
                new ItemActivateEventListener<JournalManager.JournalChapter>() {
                    @Override
                    public void onItemActivated(UIWidget widget, JournalManager.JournalChapter item) {
                        updateJournal();
                    }
                });
    }

    public void refreshJournal() {
        selectedChapter = null;
        updateChapters();
        List<JournalManager.JournalChapter> chapters = chapterList.getList();
        if (chapters.size() > 0) {
            chapterList.setSelection(chapters.get(0));
        }
    }

    @Override
    public void update(float delta) {
        JournalManager.JournalChapter selection = chapterList.getSelection();
        if (selection != selectedChapter) {
            selectedChapter = selection;
            updateJournal();
        }
    }

    private void updateChapters() {
        JournalManager journalManager = CoreRegistry.get(JournalManager.class);
        EntityRef playerEntity = CoreRegistry.get(LocalPlayer.class).getCharacterEntity();
        Map<JournalManager.JournalChapter, DocumentData> playerEntries = journalManager.getPlayerEntries(playerEntity);

        List<JournalManager.JournalChapter> entries = new LinkedList<>();

        for (JournalManager.JournalChapter journalChapter : playerEntries.keySet()) {
            entries.add(journalChapter);
        }

        chapterList.setList(entries);
    }

    private void updateJournal() {
        JournalManager journalManager = CoreRegistry.get(JournalManager.class);
        EntityRef playerEntity = CoreRegistry.get(LocalPlayer.class).getCharacterEntity();
        Map<JournalManager.JournalChapter, DocumentData> playerEntries = journalManager.getPlayerEntries(playerEntity);

        journalList.navigateTo(playerEntries.get(selectedChapter));
        JournalAccessComponent journal = playerEntity.getComponent(JournalAccessComponent.class);
        Map<String, List<String>> discoveredEntries = journal.discoveredJournalEntries;
        List<String> discoveredChapterEntries = discoveredEntries.get(selectedChapter.getChapterId());
        List<String> updatedChapterEntries = new LinkedList<>();
        for (String discoveredChapterEntryId : discoveredChapterEntries) {
            String[] entrySplit = discoveredChapterEntryId.split("\\|", 3);
            updatedChapterEntries.add(entrySplit[0] + "|" + entrySplit[1] + "|" + "read");
        }
        discoveredEntries.put(selectedChapter.getChapterId(), updatedChapterEntries);
        playerEntity.saveComponent(journal);
    }
}
