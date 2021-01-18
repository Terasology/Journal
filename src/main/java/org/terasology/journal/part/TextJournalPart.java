// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal.part;

import org.terasology.joml.geom.Rectanglei;
import org.terasology.journal.JournalManager;
import org.joml.Vector2i;
import org.terasology.nui.asset.font.Font;
import org.terasology.nui.Canvas;
import org.terasology.nui.HorizontalAlign;
import org.terasology.nui.TextLineBuilder;

import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class TextJournalPart implements JournalManager.JournalEntryPart {
    private String text;
    private HorizontalAlign horizontalAlign;

    public TextJournalPart(String text) {
        this(text, HorizontalAlign.LEFT);
    }

    public TextJournalPart(String text, HorizontalAlign horizontalAlign) {
        this.text = text;
        this.horizontalAlign = horizontalAlign;
    }

    @Override
    public Vector2i getPreferredSize(long date) {
        Canvas canvas = null;
        Font font = canvas.getCurrentStyle().getFont();

        List<String> lines = TextLineBuilder.getLines(font, text, canvas.size().x);
        Vector2i result = font.getSize(lines);
        return new Vector2i(result.x, result.y + 3);
    }

    @Override
    public void render(Canvas canvas, Rectanglei region, long date) {
        canvas.getCurrentStyle().setHorizontalTextAlignment(horizontalAlign);
        canvas.drawText(text, region);
    }
}
