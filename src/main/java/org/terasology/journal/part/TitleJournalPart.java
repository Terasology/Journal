// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal.part;

import org.joml.Rectanglei;
import org.joml.Vector2i;
import org.terasology.engine.rendering.assets.font.Font;
import org.terasology.engine.utilities.Assets;
import org.terasology.journal.JournalManager;
import org.terasology.nui.Canvas;
import org.terasology.nui.Color;
import org.terasology.nui.HorizontalAlign;
import org.terasology.nui.TextLineBuilder;
import org.terasology.nui.VerticalAlign;

import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class TitleJournalPart implements JournalManager.JournalEntryPart {
    private final String text;
    private final Font font;

    public TitleJournalPart(String text) {
        this.text = text;
        font = Assets.getFont("engine:NotoSans-Regular-Title").get();
    }

    @Override
    public Vector2i getPreferredSize(long date) {
        Canvas canvas = null;

        List<String> lines = TextLineBuilder.getLines(font, text, canvas.size().x);
        Vector2i size = font.getSize(lines);
        return new Vector2i(size.x, size.y + 5);
    }

    @Override
    public void render(Canvas canvas, Rectanglei region, long date) {
        canvas.drawTextRaw(text, font, Color.BLACK, region, HorizontalAlign.CENTER, VerticalAlign.MIDDLE);
    }
}
