// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.network.NetworkEvent;
import org.terasology.engine.network.OwnerEvent;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
@OwnerEvent
public class NewJournalEntryDiscoveredEvent extends NetworkEvent {
    private String chapterId;
    private String entryId;

    public NewJournalEntryDiscoveredEvent() {
    }

    public NewJournalEntryDiscoveredEvent(String chapterId, String entryId) {
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
