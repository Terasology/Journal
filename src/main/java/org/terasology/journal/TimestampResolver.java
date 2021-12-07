// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

public final class TimestampResolver {
    private TimestampResolver() {
    }

    public static String getJournalEntryDate(long date) {
        long days = date / (24 * 60 * 60 * 1000);
        long hours = (date / (60 * 60 * 1000)) % 24;
        long minutes = (date / (60 * 1000)) % 60;
        long seconds = (date / 1000) % 60;

        return "Day " + (days + 1) + ", " + format(hours) + ":" + format(minutes) + ":" + format(seconds);
    }

    private static String format(long no) {
        String s = String.valueOf(no);
        if (s.length() == 2) {
            return s;
        } else {
            return "0" + s;
        }
    }
}
