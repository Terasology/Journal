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

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.assets.texture.TextureRegion;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.widgets.browser.data.DocumentData;

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
    }

    public interface JournalEntryPart {
        Vector2i getPreferredSize(long date);

        void render(Canvas canvas, Rect2i region, long date);
    }
}
