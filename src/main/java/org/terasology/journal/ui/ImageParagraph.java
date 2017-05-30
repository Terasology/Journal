/*
 * Copyright 2017 MovingBlocks
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

import org.terasology.entitySystem.prefab.Prefab;
import org.terasology.logic.common.DisplayNameComponent;
import org.terasology.logic.inventory.ItemComponent;
import org.terasology.math.geom.Rect2i;
import org.terasology.math.geom.Vector2i;
import org.terasology.rendering.nui.Canvas;
import org.terasology.rendering.nui.HorizontalAlign;
import org.terasology.rendering.nui.layers.ingame.inventory.ItemIcon;
import org.terasology.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.rendering.nui.widgets.browser.data.basic.flow.ContainerRenderSpace;
import org.terasology.rendering.nui.widgets.browser.ui.ParagraphRenderable;
import org.terasology.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;
import org.terasology.utilities.Assets;
import org.terasology.world.block.Block;

public class ImageParagraph implements ParagraphData, ParagraphRenderable {

    private int iconSize = 64;
    private int indentAbove = 5;
    private int indentBelow = 5;
    private int iconSpacing = 3;
    private ItemIcon[] icons;

    public ImageParagraph(Prefab[] itemPrefabs, Block[] blocks) {
        int imageCount = (itemPrefabs != null ? itemPrefabs.length : 0) + (blocks != null ? blocks.length : 0);
        icons = new ItemIcon[imageCount];
        if (itemPrefabs != null) {
            for (int i = 0; i < itemPrefabs.length; i++) {
                ItemComponent item = itemPrefabs[i].getComponent(ItemComponent.class);
                DisplayNameComponent displayName = itemPrefabs[i].getComponent(DisplayNameComponent.class);
                ItemIcon itemIcon = new ItemIcon();
                itemIcon.setIcon(item.icon);
                if (displayName != null) {
                    itemIcon.setTooltip(displayName.name);
                }
                icons[i] = itemIcon;
            }
        }
        if (blocks != null) {
            for (int i = 0; i < blocks.length; i++) {
                ItemIcon itemIcon = new ItemIcon();
                itemIcon.setMesh(blocks[i].getMesh());
                itemIcon.setMeshTexture(Assets.getTexture("engine:terrain").get());
                itemIcon.setTooltip(blocks[i].getDisplayName());
                icons[i + itemPrefabs.length] = itemIcon;
            }
        }
    }

    @Override
    public ParagraphRenderStyle getParagraphRenderStyle() {
        return new ParagraphRenderStyle() {
            @Override
            public HorizontalAlign getHorizontalAlignment() {
                return HorizontalAlign.CENTER;
            }
        };
    }

    @Override
    public ParagraphRenderable getParagraphContents() {
        return this;
    }

    @Override
    public void renderContents(Canvas canvas, Vector2i startPos, ContainerRenderSpace containerRenderSpace, int leftIndent, int rightIndent, ParagraphRenderStyle defaultStyle, HorizontalAlign horizontalAlign, HyperlinkRegister hyperlinkRegister) {
        int imageCount = icons.length;
        int drawingWidth = imageCount * iconSize + (imageCount - 1) * imageCount;
        int x = startPos.x + horizontalAlign.getOffset(drawingWidth, containerRenderSpace.getWidthForVerticalPosition(startPos.y));
        int y = startPos.y + indentAbove;
        for (ItemIcon icon : icons) {
            canvas.drawWidget(icon, Rect2i.createFromMinAndSize(x, y, iconSize, iconSize));
            x += iconSize + iconSpacing;
        }
    }

    @Override
    public int getPreferredContentsHeight(ParagraphRenderStyle defaultStyle, int yStart, ContainerRenderSpace containerRenderSpace, int sideIndents) {
        return getPreferredSize().y;
    }

    @Override
    public int getContentsMinWidth(ParagraphRenderStyle defaultStyle) {
        return getPreferredSize().x;
    }

    private Vector2i getPreferredSize() {
        return new Vector2i(iconSize, indentAbove + iconSize + indentBelow);
    }
}
