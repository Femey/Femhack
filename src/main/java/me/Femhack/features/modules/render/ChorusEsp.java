package me.Femhack.features.modules.render;

import me.Femhack.event.events.ChorusEvent;
import me.Femhack.event.events.Render3DEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import me.Femhack.util.RenderUtil;
import me.Femhack.util.Timer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

public class ChorusEsp extends Module {

    private final Timer timer;

    public ChorusEsp() {
        super("ChorusEsp", "Shows where people chorus (chorus delay counter)", Category.RENDER, true, false, false);
        timer = new Timer();

    }

    public static ChorusEsp instance;

    public Setting<Integer> red = this.register(new Setting<Integer>("Red", 255, 0, 255));
    public Setting<Integer> green = this.register(new Setting<Integer>("Green", 0, 0, 255));
    public Setting<Integer> blue = this.register(new Setting<Integer>("Blue", 255, 0, 255));
    public Setting<Integer> alpha = this.register(new Setting<Integer>("Alpha", 255, 0, 255));
    public Setting<Integer> time = this.register(new Setting<Integer>("Duration", 500, 50, 3000));

    private float x;
    private float y;
    private float z;

    @SubscribeEvent
    public void onChorus(final ChorusEvent event) {
        this.x = (float) event.getChorusX();
        this.y = (float) event.getChorusY();
        this.z = (float) event.getChorusZ();
        this.timer.reset();
    }

    @Override
    public void onRender3D(Render3DEvent render3DEvent){
        if (!this.timer.passedMs(this.time.getValue())) {
            RenderUtil.drawCircle(this.x, this.y, this.z, 0.4f, new Color(this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue()));
        }
    }
}