// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal.ui;

import org.terasology.engine.rendering.nui.CoreScreenLayer;
import org.terasology.nui.Canvas;

public class NewEntryWindow extends CoreScreenLayer {

    private float alpha = 1f;

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public void initialise() {
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.setAlpha(alpha);

        super.onDraw(canvas);

        canvas.setAlpha(1f);
    }

    @Override
    public boolean isModal() {
        return false;
    }

    @Override
    public boolean isReleasingMouse() {
        return false;
    }

    @Override
    public boolean isEscapeToCloseAllowed() {
        return false;
    }
}
