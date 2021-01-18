// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal.ui;

import org.terasology.joml.geom.Rectanglei;
import org.terasology.journal.JournalManager;
import org.joml.Vector2i;
import org.terasology.nui.Canvas;
import org.terasology.nui.itemRendering.ItemRenderer;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class JournalChapterRenderer implements ItemRenderer<JournalManager.JournalChapter> {
    @Override
    public void draw(JournalManager.JournalChapter value, Canvas canvas) {
        draw(value, canvas, canvas.getRegion());
    }

    @Override
    public void draw(JournalManager.JournalChapter value, Canvas canvas, Rectanglei subregion) {
        canvas.drawTexture(value.getTexture(), subregion);
    }

    @Override
    public Vector2i getPreferredSize(JournalManager.JournalChapter value, Canvas canvas) {
        return new Vector2i(50, 50);
    }

    @Override
    public String getTooltip(JournalManager.JournalChapter value) {
        return value.getChapterName();
    }
}
