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

import com.google.common.base.Supplier;
import org.terasology.journal.JournalManager;
import org.terasology.math.Rect2i;
import org.terasology.math.Vector2i;
import org.terasology.rendering.assets.font.Font;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.HorizontalAlign;
import org.terasology.rendering.nui.TextLineBuilder;

import java.util.List;

public class DynamicTextJournalPart implements JournalManager.JournalEntryPart {
    private Supplier<String> text;
    private HorizontalAlign horizontalAlign;

    public DynamicTextJournalPart(Supplier<String> text) {
        this(text, HorizontalAlign.LEFT);
    }

    public DynamicTextJournalPart(Supplier<String> text, HorizontalAlign horizontalAlign) {
        this.text = text;
        this.horizontalAlign = horizontalAlign;
    }

    @Override
    public Vector2i getPreferredSize(Canvas canvas, long date) {
        Font font = canvas.getCurrentStyle().getFont();

        List<String> lines = TextLineBuilder.getLines(font, text.get(), canvas.size().x);
        Vector2i result = font.getSize(lines);
        return new Vector2i(result.x, result.y + 3);
    }

    @Override
    public void render(Canvas canvas, Rect2i region, long date) {
        canvas.getCurrentStyle().setHorizontalTextAlignment(horizontalAlign);
        canvas.drawText(text.get(), region);
    }
}
