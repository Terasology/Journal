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

import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.BeforeEntityCreated;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.registry.In;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class JournalAuthoritySystem extends BaseComponentSystem {
    @In
    private Time time;

    @ReceiveEvent
    public void addJournalAccessComponentToPlayers(BeforeEntityCreated event, EntityRef character) {
        if (event.getPrefab() != null && event.getPrefab().getName().equals("engine:player")) {
            JournalAccessComponent journalAccess = new JournalAccessComponent();
            journalAccess.discoveredJournalEntries = new LinkedHashMap<>();
            event.addComponent(journalAccess);
        }
    }

    @ReceiveEvent
    public void newJournalEntryDiscovered(DiscoveredNewJournalEntry event, EntityRef character,
                                          JournalAccessComponent journalAccess) {
        // Apply the changes to the server object
        String chapterId = event.getChapterId();
        List<String> entries = journalAccess.discoveredJournalEntries.get(chapterId);
        if (entries == null) {
            entries = new LinkedList<>();
        } else {
            entries = new LinkedList<>(entries);
        }
        journalAccess.discoveredJournalEntries.put(chapterId, entries);

        entries.add(time.getGameTimeInMs() + "|" + event.getEntryId() + "|" + "unread");
        character.saveComponent(journalAccess);

        // Notify the client
        character.send(new NewJournalEntryDiscoveredEvent(chapterId, event.getEntryId()));
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
