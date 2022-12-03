package me.Femhack.features.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.Femhack.Femhack;
import me.Femhack.features.command.Command;

public class HelpCommand
        extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String[] commands) {
        HelpCommand.sendMessage("Commands: ");
        for (Command command : Femhack.commandManager.getCommands()) {
            HelpCommand.sendMessage(ChatFormatting.GRAY + Femhack.commandManager.getPrefix() + command.getName());
        }
    }
}

