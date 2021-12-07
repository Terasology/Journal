// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.core.Time;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.network.ClientComponent;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.gestalt.entitysystem.event.ReceiveEvent;
import org.terasology.input.ButtonState;
import org.terasology.journal.ui.JournalNUIWindow;
import org.terasology.journal.ui.NewEntryWindow;

@RegisterSystem(RegisterMode.CLIENT)
public class JournalClientSystem extends BaseComponentSystem implements UpdateSubscriberSystem {
    private static final long FULL_ALPHA_TIME = 3000;
    private static final long DIM_ALPHA_TIME = 1500;

    @In
    private NUIManager nuiManager;
    @In
    private Time time;

    private long lastNotificationReceived;

    @Override
    public void update(float delta) {
        long currentTime = time.getGameTimeInMs();
        float alpha = (currentTime > lastNotificationReceived + FULL_ALPHA_TIME + DIM_ALPHA_TIME) ? 0f
                : currentTime > lastNotificationReceived + FULL_ALPHA_TIME
                ? 1f - ((currentTime - lastNotificationReceived - FULL_ALPHA_TIME) / (1f * DIM_ALPHA_TIME)) : 1f;

        NewEntryWindow window = ((NewEntryWindow) nuiManager.getScreen("Journal:NewEntryWindow"));
        if (alpha == 0f && window != null) {
            nuiManager.closeScreen(window);
        } else if (alpha > 0f) {
            if (window == null) {
                window = (NewEntryWindow) nuiManager.pushScreen("Journal:NewEntryWindow");
            }
            window.setAlpha(alpha);
        }
    }

    @ReceiveEvent
    public void openJournal(JournalButton event, EntityRef character,
                            ClientComponent clientComponent) {
        if (event.getState() == ButtonState.DOWN) {
            nuiManager.toggleScreen("Journal:JournalWindow");
            JournalNUIWindow window = (JournalNUIWindow) nuiManager.getScreen("Journal:JournalWindow");
            window.refreshJournal();
        }
    }

    @ReceiveEvent
    public void newEntryNotificationReceived(NewJournalEntryDiscoveredEvent event, EntityRef character) {
        lastNotificationReceived = time.getGameTimeInMs();
    }
}
