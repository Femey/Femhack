package me.Femhack.event.events;

import me.Femhack.event.EventStage;

public class RenderItemEvent extends EventStage
{
    double mainX;
    double mainY;
    double mainZ;
    double offX;
    double offY;
    double offZ;
    double mainRotX;
    double mainRotY;
    double mainRotZ;
    double offRotX;
    double offRotY;
    double offRotZ;
    double mainHandScaleX;
    double mainHandScaleY;
    double mainHandScaleZ;
    double offHandScaleX;
    double offHandScaleY;
    double offHandScaleZ;

    public RenderItemEvent(final double mainX, final double mainY, final double mainZ, final double offX, final double offY, final double offZ, final double mainRotX, final double mainRotY, final double mainRotZ, final double offRotX, final double offRotY, final double offRotZ, final double mainHandScaleX, final double mainHandScaleY, final double mainHandScaleZ, final double offHandScaleX, final double offHandScaleY, final double offHandScaleZ) {
        this.mainX = mainX;
        this.mainY = mainY;
        this.mainZ = mainZ;
        this.offX = offX;
        this.offY = offY;
        this.offZ = offZ;
        this.mainRotX = mainRotX;
        this.mainRotY = mainRotY;
        this.mainRotZ = mainRotZ;
        this.offRotX = offRotX;
        this.offRotY = offRotY;
        this.offRotZ = offRotZ;
        this.mainHandScaleX = mainHandScaleX;
        this.mainHandScaleY = mainHandScaleY;
        this.mainHandScaleZ = mainHandScaleZ;
        this.offHandScaleX = offHandScaleX;
        this.offHandScaleY = offHandScaleY;
        this.offHandScaleZ = offHandScaleZ;
    }

    public double getMainX() {
        return this.mainX;
    }

    public void setMainX(final double v) {
        this.mainX = v;
    }

    public double getMainY() {
        return this.mainY;
    }

    public void setMainY(final double v) {
        this.mainY = v;
    }

    public double getMainZ() {
        return this.mainZ;
    }

    public void setMainZ(final double v) {
        this.mainZ = v;
    }

    public double getOffX() {
        return this.offX;
    }

    public void setOffX(final double v) {
        this.offX = v;
    }

    public double getOffY() {
        return this.offY;
    }

    public void setOffY(final double v) {
        this.offY = v;
    }

    public double getOffZ() {
        return this.offZ;
    }

    public void setOffZ(final double v) {
        this.offZ = v;
    }

    public double getMainRotX() {
        return this.mainRotX;
    }

    public void setMainRotX(final double v) {
        this.mainRotX = v;
    }

    public double getMainRotY() {
        return this.mainRotY;
    }

    public void setMainRotY(final double v) {
        this.mainRotY = v;
    }

    public double getMainRotZ() {
        return this.mainRotZ;
    }

    public void setMainRotZ(final double v) {
        this.mainRotZ = v;
    }

    public double getOffRotX() {
        return this.offRotX;
    }

    public void setOffRotX(final double v) {
        this.offRotX = v;
    }

    public double getOffRotY() {
        return this.offRotY;
    }

    public void setOffRotY(final double v) {
        this.offRotY = v;
    }

    public double getOffRotZ() {
        return this.offRotZ;
    }

    public void setOffRotZ(final double v) {
        this.offRotZ = v;
    }

    public double getMainHandScaleX() {
        return this.mainHandScaleX;
    }

    public void setMainHandScaleX(final double v) {
        this.mainHandScaleX = v;
    }

    public double getMainHandScaleY() {
        return this.mainHandScaleY;
    }

    public void setMainHandScaleY(final double v) {
        this.mainHandScaleY = v;
    }

    public double getMainHandScaleZ() {
        return this.mainHandScaleZ;
    }

    public void setMainHandScaleZ(final double v) {
        this.mainHandScaleZ = v;
    }

    public double getOffHandScaleX() {
        return this.offHandScaleX;
    }

    public void setOffHandScaleX(final double v) {
        this.offHandScaleX = v;
    }

    public double getOffHandScaleY() {
        return this.offHandScaleY;
    }

    public void setOffHandScaleY(final double v) {
        this.offHandScaleY = v;
    }

    public double getOffHandScaleZ() {
        return this.offHandScaleZ;
    }

    public void setOffHandScaleZ(final double v) {
        this.offHandScaleZ = v;
    }
}

