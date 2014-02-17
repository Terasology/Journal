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

import org.terasology.journal.JournalManager;
import org.terasology.math.Rect2i;
import org.terasology.math.Vector2i;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.itemRendering.ItemRenderer;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class JournalEntryRenderer implements ItemRenderer<JournalManager.JournalEntry> {
    @Override
    public void draw(JournalManager.JournalEntry value, Canvas canvas, Rect2i subregion) {
        int y = 0;

        for (JournalManager.JournalEntryPart part : value.getParts()) {
            Vector2i preferredSize = part.getPreferredSize(canvas, value.getDate());
            part.render(canvas, Rect2i.createFromMinAndMax(subregion.minX(), subregion.minY() + y, subregion.maxX(), subregion.minY() + y + preferredSize.y), value.getDate());
            y += preferredSize.y;
        }
    }

    @Override
    public void draw(JournalManager.JournalEntry value, Canvas canvas) {
        draw(value, canvas, canvas.getRegion());
    }

    @Override
    public Vector2i getPreferredSize(JournalManager.JournalEntry value, Canvas canvas) {
        int x = 0;
        int y = 0;

        for (JournalManager.JournalEntryPart part : value.getParts()) {
            Vector2i preferredSize = part.getPreferredSize(canvas, value.getDate());
            x = Math.max(x, preferredSize.x);
            y += preferredSize.y;
        }

        return new Vector2i(x, y);
    }

    @Override
    public String getTooltip(JournalManager.JournalEntry value) {
        return null;
    }
}
