// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal.part;

import org.joml.primitives.Rectanglei;
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
public class TimestampJournalPart implements JournalManager.JournalEntryPart {
    private int indentAboveDate = 15;
    private int indentBelowDate = 10;

    @Override
    public Vector2i getPreferredSize(long date) {
        Canvas canvas = null;
        Font font = canvas.getCurrentStyle().getFont();

        List<String> dateLines = TextLineBuilder.getLines(font, getJournalEntryDate(date), canvas.size().x);
        Vector2i size = font.getSize(dateLines);
        return new Vector2i(size.x, indentAboveDate + size.y + indentBelowDate);
    }

    @Override
    public void render(Canvas canvas, Rectanglei region, long date) {
        Font font = canvas.getCurrentStyle().getFont();

        List<String> dateLines = TextLineBuilder.getLines(font, getJournalEntryDate(date), canvas.size().x);
        Vector2i dateSize = font.getSize(dateLines);

        canvas.getCurrentStyle().setHorizontalTextAlignment(HorizontalAlign.CENTER);
        canvas.drawText(getJournalEntryDate(date), new Rectanglei(region.minX, indentAboveDate + region.minY, region.maxX, indentAboveDate + region.minY + dateSize.y));
    }

    private String format(long no) {
        String s = String.valueOf(no);
        if (s.length() == 2) {
            return s;
        } else {
            return "0" + s;
        }
    }

    private String getJournalEntryDate(long date) {
        long days = date / (24 * 60 * 60 * 1000);
        long hours = (date / (60 * 60 * 1000)) % 24;
        long minutes = (date / (60 * 1000)) % 60;
        long seconds = (date / 1000) % 60;

        return "Day " + (days + 1) + ", " + format(hours) + ":" + format(minutes) + ":" + format(seconds);
    }
}
