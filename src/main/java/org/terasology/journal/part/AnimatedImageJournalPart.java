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

import org.terasology.engine.Time;
import org.terasology.journal.JournalManager;
import org.terasology.math.Rect2i;
import org.terasology.math.Vector2i;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.HorizontalAlign;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class AnimatedImageJournalPart implements JournalManager.JournalEntryPart {
    private Vector2i size;
    private HorizontalAlign horizontalAlign;

    private List<ImageDefinition> imageDefinitions = new LinkedList<>();
    private long cycleLength;

    public AnimatedImageJournalPart(Vector2i size, HorizontalAlign horizontalAlign) {
        this.size = size;
        this.horizontalAlign = horizontalAlign;
    }

    public void addFrame(Texture texture, long duration) {
        imageDefinitions.add(new ImageDefinition(texture, duration));
        cycleLength += duration;
    }

    @Override
    public Vector2i getPreferredSize(Canvas canvas, long date) {
        return size;
    }

    @Override
    public void render(Canvas canvas, Rect2i region, long date) {
        int x = horizontalAlign.getOffset(size.getX(), region.width());

        long gameTime = CoreRegistry.get(Time.class).getGameTimeInMs();

        long cycleTime = gameTime % cycleLength;

        Texture texture = getImageForCycleTime(cycleTime);

        canvas.drawTexture(texture, Rect2i.createFromMinAndMax(region.minX() + x, region.minY(), region.minX() + x + texture.getWidth(), region.maxY()));
    }

    private Texture getImageForCycleTime(long cycleTime) {
        long time = 0;
        for (ImageDefinition imageDefinition : imageDefinitions) {
            if (time + imageDefinition.duration > cycleTime) {
                return imageDefinition.texture;
            }
            time += imageDefinition.duration;
        }
        return null;
    }

    private class ImageDefinition {
        private Texture texture;
        private long duration;

        private ImageDefinition(Texture texture, long duration) {
            this.texture = texture;
            this.duration = duration;
        }
    }
}
