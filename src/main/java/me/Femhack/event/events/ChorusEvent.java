package me.Femhack.event.events;

import me.Femhack.event.EventStage;

public class ChorusEvent extends EventStage {
    private final double chorusX;
    private final double chorusY;
    private final double chorusZ;

    public static ChorusEvent instance;

    public ChorusEvent(final double x, final double y, final double z) {
        this.chorusX = x;
        this.chorusY = y;
        this.chorusZ = z;
    }

    public double getChorusX() {
        return this.chorusX;
    }

    public double getChorusY() {
        return this.chorusY;
    }

    public double getChorusZ() {
        return this.chorusZ;
    }
}
