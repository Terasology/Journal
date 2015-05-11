/*
 * Copyright 2015 MovingBlocks
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
