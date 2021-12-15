// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.rendering.nui.widgets.browser.data.ParagraphData;

import java.util.Collection;

public interface JournalEntryProducer {
    Collection<ParagraphData> produceParagraph(long date);
}
