package me.Femhack.features.command.commands;

import me.Femhack.OyVey;
import me.Femhack.features.command.Command;

public class ReloadCommand
        extends Command {
    public ReloadCommand() {
        super("reload", new String[0]);
    }

    @Override
    public void execute(String[] commands) {
        OyVey.reload();
    }
}

