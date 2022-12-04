package me.Femhack.features.command.commands;

import me.Femhack.features.command.Command;
import me.Femhack.features.modules.player.FakePlayer;

public class FakeplayerCommand
        extends Command {
    public FakeplayerCommand() {
        super("fakeplayer");
    }

    @Override
    public void execute(String[] commands) {
        FakePlayer.getInstance().toggle();
    }
}