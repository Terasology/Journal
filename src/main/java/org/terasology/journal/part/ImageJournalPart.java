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
package org.terasology.journal.part;

import org.terasology.journal.JournalManager;
import org.terasology.math.Rect2i;
import org.terasology.math.Vector2i;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.HorizontalAlign;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class ImageJournalPart implements JournalManager.JournalEntryPart {
    private Texture texture;
    private HorizontalAlign horizontalAlign;

    public ImageJournalPart(Texture texture, HorizontalAlign horizontalAlign) {
        this.texture = texture;
        this.horizontalAlign = horizontalAlign;
    }

    @Override
    public Vector2i getPreferredSize(Canvas canvas) {
        return texture.size();
    }

    @Override
    public void render(Canvas canvas, Rect2i region) {
        int x = horizontalAlign.getOffset(texture.getWidth(), region.width());
        canvas.drawTexture(texture, Rect2i.createFromMinAndMax(region.minX() + x, region.minY(), region.minX() + x + texture.getWidth(), region.maxY()));
    }
}
