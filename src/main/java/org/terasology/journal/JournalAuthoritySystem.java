// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.registry.In;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@RegisterSystem(RegisterMode.AUTHORITY)
public class JournalAuthoritySystem extends BaseComponentSystem {
    @In
    private Time time;
    @In
    private JournalManager journalManager;

    @ReceiveEvent
    public void newJournalEntryDiscovered(DiscoveredNewJournalEntry event, EntityRef character,
                                          JournalAccessComponent journalAccess) {
        // Apply the changes to the server object
        String chapterId = event.getChapterId();
        String entryId = event.getEntryId();

        boolean entryAlreadyExists = journalManager.hasEntry(character, chapterId, entryId);
        if (!entryAlreadyExists) {
            List<String> entries = journalAccess.discoveredJournalEntries.get(chapterId);
            if (entries == null) {
                entries = new LinkedList<>();
            } else {
                entries = new LinkedList<>(entries);
            }
            entries.add(time.getGameTimeInMs() + "|" + entryId + "|" + "unread");
            journalAccess.discoveredJournalEntries.put(chapterId, entries);
            character.saveComponent(journalAccess);

            // Notify the client
            character.send(new NewJournalEntryDiscoveredEvent(chapterId, entryId));
        }
    }

    @ReceiveEvent
    public void journalEntryRemove(RemoveJournalEntry event, EntityRef character,
                                   JournalAccessComponent journalAccess) {
        // Apply the changes to the server object
        String chapterId = event.getChapterId();
        List<String> entries = journalAccess.discoveredJournalEntries.get(chapterId);
        if (entries == null) {
            entries = new LinkedList<>();
            journalAccess.discoveredJournalEntries.put(chapterId, entries);
        } else {
            entries = new LinkedList<>(entries);
            journalAccess.discoveredJournalEntries.put(chapterId, entries);
        }

        boolean changed = false;
        Iterator<String> entryIterator = entries.iterator();
        while (entryIterator.hasNext()) {
            String entry = entryIterator.next();
            if (entry.contains("|" + event.getEntryId())) {
                entryIterator.remove();
                changed = true;
            }
        }
        if (changed) {
            character.saveComponent(journalAccess);
        }
    }
}
