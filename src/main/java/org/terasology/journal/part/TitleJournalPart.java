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

import org.joml.primitives.Rectanglei;
import org.terasology.utilities.Assets;
import org.terasology.journal.JournalManager;
import org.joml.Vector2i;
import org.terasology.rendering.assets.font.Font;
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
    private String text;
    private Font font;

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
