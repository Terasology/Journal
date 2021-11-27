// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal.ui;

import org.joml.Vector2i;
import org.terasology.engine.entitySystem.prefab.Prefab;
import org.terasology.engine.logic.common.DisplayNameComponent;
import org.terasology.engine.logic.inventory.ItemComponent;
import org.terasology.engine.rendering.nui.widgets.browser.data.ParagraphData;
import org.terasology.engine.rendering.nui.widgets.browser.data.basic.flow.ContainerRenderSpace;
import org.terasology.engine.rendering.nui.widgets.browser.ui.ParagraphRenderable;
import org.terasology.engine.rendering.nui.widgets.browser.ui.style.ParagraphRenderStyle;
import org.terasology.engine.utilities.Assets;
import org.terasology.engine.world.block.Block;
import org.terasology.joml.geom.Rectanglei;
import org.terasology.nui.Canvas;
import org.terasology.nui.HorizontalAlign;
import org.terasology.module.inventory.ui.ItemIcon;

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
                itemIcon.setMesh(blocks[i].getMeshGenerator().getStandaloneMesh());
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
            canvas.drawWidget(icon, new Rectanglei(x, y).setSize(iconSize, iconSize));
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
