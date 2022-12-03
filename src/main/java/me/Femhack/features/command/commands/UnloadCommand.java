package me.Femhack.features.command.commands;

import me.Femhack.Femhack;
import me.Femhack.features.command.Command;

public class UnloadCommand
        extends Command {
    public UnloadCommand() {
        super("unload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        Femhack.unload(true);
    }
}

