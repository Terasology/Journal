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
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.HorizontalAlign;
import org.terasology.rendering.nui.TextLineBuilder;
import org.terasology.rendering.nui.itemRendering.ItemRenderer;

import java.util.List;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class JournalEntryRenderer implements ItemRenderer<JournalManager.JournalEntry> {
    private int indentAboveDate = 15;
    private int indentBelowDate = 10;

    @Override
    public void draw(JournalManager.JournalEntry value, Canvas canvas, Rect2i subregion) {
        int y = indentAboveDate;

        Font font = canvas.getCurrentStyle().getFont();

        List<String> dateLines = TextLineBuilder.getLines(font, getJournalEntryDate(value), canvas.size().x);
        Vector2i dateSize = font.getSize(dateLines);

        canvas.getCurrentStyle().setHorizontalTextAlignment(HorizontalAlign.CENTER);
        canvas.drawText(getJournalEntryDate(value), Rect2i.createFromMinAndMax(subregion.minX(), subregion.minY() + y, subregion.maxX(), subregion.minY() + y + dateSize.y));

        y += dateSize.y;

        y += indentBelowDate;

        for (JournalManager.JournalEntryPart part : value.getParts()) {
            Vector2i preferredSize = part.getPreferredSize(canvas);
            part.render(canvas, Rect2i.createFromMinAndMax(subregion.minX(), subregion.minY() + y, subregion.maxX(), subregion.minY() + y + preferredSize.y));
            y += preferredSize.y;
        }
    }

    @Override
    public void draw(JournalManager.JournalEntry value, Canvas canvas) {
        draw(value, canvas, canvas.getRegion());
    }

    @Override
    public Vector2i getPreferredSize(JournalManager.JournalEntry value, Canvas canvas) {
        Font font = canvas.getCurrentStyle().getFont();

        List<String> dateLines = TextLineBuilder.getLines(font, getJournalEntryDate(value), canvas.size().x);
        Vector2i dateSize = font.getSize(dateLines);

        int x = dateSize.x;
        int y = indentAboveDate + dateSize.y + indentBelowDate;

        for (JournalManager.JournalEntryPart part : value.getParts()) {
            Vector2i preferredSize = part.getPreferredSize(canvas);
            x = Math.max(x, preferredSize.x);
            y += preferredSize.y;
        }

        return new Vector2i(x, y);
    }

    private String getJournalEntryDate(JournalManager.JournalEntry value) {
        long timeInMillis = value.getDate();
        long days = timeInMillis / (24 * 60 * 60 * 1000);
        long hours = (timeInMillis / (60 * 60 * 1000)) % 24;
        long minutes = (timeInMillis / (60 * 1000)) % 60;
        long seconds = (timeInMillis / 1000) % 60;

        return "Day " + (days + 1) + ", " + format(hours) + ":" + format(minutes) + ":" + format(seconds);
    }

    private String format(long no) {
        String s = String.valueOf(no);
        if (s.length() == 2) {
            return s;
        } else {
            return "0" + s;
        }
    }


    @Override
    public String getTooltip(JournalManager.JournalEntry value) {
        return null;
    }
}
