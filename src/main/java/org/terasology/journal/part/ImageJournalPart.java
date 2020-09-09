// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal.part;

import org.joml.Rectanglei;
import org.joml.Vector2i;
import org.terasology.engine.rendering.assets.texture.TextureRegion;
import org.terasology.journal.JournalManager;
import org.terasology.nui.Canvas;
import org.terasology.nui.HorizontalAlign;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class ImageJournalPart implements JournalManager.JournalEntryPart {
    private final TextureRegion texture;
    private final HorizontalAlign horizontalAlign;

    public ImageJournalPart(TextureRegion texture, HorizontalAlign horizontalAlign) {
        this.texture = texture;
        this.horizontalAlign = horizontalAlign;
    }

    @Override
    public Vector2i getPreferredSize(long date) {
        return texture.size();
    }

    @Override
    public void render(Canvas canvas, Rectanglei region, long date) {
        int x = horizontalAlign.getOffset(texture.getWidth(), region.lengthX());
        canvas.drawTexture(texture, new Rectanglei(region.minX + x, region.minY, region.minX + x + texture.getWidth()
                , region.maxY));
    }
}
