// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.journal;

import org.terasology.engine.input.BindButtonEvent;
import org.terasology.engine.input.DefaultBinding;
import org.terasology.engine.input.RegisterBindButton;
import org.terasology.input.InputType;
import org.terasology.input.Keyboard;

@RegisterBindButton(id = "openJournal", description = "Open journal", category = "interaction")
@DefaultBinding(type = InputType.KEY, id = Keyboard.KeyId.J)
public class JournalButton extends BindButtonEvent {
}
