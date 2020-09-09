// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.rendering.nui.widgets.browser.data.ParagraphData;

import java.util.Collection;

public interface JournalChapterHandler {
    Collection<ParagraphData> resolveJournalEntryParts(String entryId, long date);
}
