// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal.part;

import org.terasology.joml.geom.Rectanglei;
import org.terasology.engine.Time;
import org.terasology.journal.JournalManager;
import org.joml.Vector2i;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.assets.texture.Texture;
import org.terasology.nui.Canvas;
import org.terasology.nui.HorizontalAlign;

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
    public Vector2i getPreferredSize(long date) {
        return size;
    }

    @Override
    public void render(Canvas canvas, Rectanglei region, long date) {
        int x = horizontalAlign.getOffset(size.x(), region.lengthX());

        long gameTime = CoreRegistry.get(Time.class).getGameTimeInMs();

        long cycleTime = gameTime % cycleLength;

        Texture texture = getImageForCycleTime(cycleTime);

        canvas.drawTexture(texture, new Rectanglei(region.minX + x, region.minY, region.minX + x + texture.getWidth(), region.maxY));
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
