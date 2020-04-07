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
import org.terasology.math.JomlUtil;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.HorizontalAlign;
import org.terasology.rendering.nui.TextLineBuilder;

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
        Vector2i size = JomlUtil.from(font.getSize(dateLines));
        return new Vector2i(size.x, indentAboveDate + size.y + indentBelowDate);
    }

    @Override
    public void render(Canvas canvas, Rect2i region, long date) {
        Font font = canvas.getCurrentStyle().getFont();

        List<String> dateLines = TextLineBuilder.getLines(font, getJournalEntryDate(date), canvas.size().x);
        Vector2i dateSize = JomlUtil.from(font.getSize(dateLines));

        canvas.getCurrentStyle().setHorizontalTextAlignment(HorizontalAlign.CENTER);
        canvas.drawText(getJournalEntryDate(date), Rect2i.createFromMinAndMax(region.minX(), indentAboveDate + region.minY(), region.maxX(), indentAboveDate + region.minY() + dateSize.y));
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
