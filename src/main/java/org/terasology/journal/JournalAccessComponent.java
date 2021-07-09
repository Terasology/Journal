// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class JournalAccessComponent implements Component<JournalAccessComponent> {
    @Replicate
    public Map<String, List<String>> discoveredJournalEntries = new LinkedHashMap<>();
}
