// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.joml.Vector2i;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.rendering.assets.texture.TextureRegion;
import org.terasology.engine.rendering.nui.widgets.browser.data.DocumentData;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.nui.Canvas;

import java.util.Map;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public interface JournalManager {
    void registerJournalChapter(String chapterId, TextureRegion icon, String name, JournalChapterHandler browserJournalChapterHandler);

    boolean hasEntry(EntityRef player, String chapterId, String entryId);

    Map<JournalChapter, DocumentData> getPlayerEntries(EntityRef player);

    public interface JournalChapter {
        String getChapterName();

        TextureRegion getTexture();

        String getChapterId();
    }

    public interface JournalEntryPart {
        Vector2i getPreferredSize(long date);

        void render(Canvas canvas, Rectanglei region, long date);
    }
}
