// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.entitySystem.event.Event;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class DiscoveredNewJournalEntry implements Event {
    private final String chapterId;
    private final String entryId;

    public DiscoveredNewJournalEntry(String chapterId, String entryId) {
        this.chapterId = chapterId;
        this.entryId = entryId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public String getEntryId() {
        return entryId;
    }
}
