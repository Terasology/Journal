// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.Replicate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcin Sciesinski <marcins78@gmail.com>
 */
public class JournalAccessComponent implements Component {
    @Replicate
    public Map<String, List<String>> discoveredJournalEntries = new LinkedHashMap<>();
}
