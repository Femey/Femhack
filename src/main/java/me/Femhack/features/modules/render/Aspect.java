package me.Femhack.features.modules.render;

import me.Femhack.event.events.PerspectiveEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Aspect extends Module {
    public Aspect() {
        super("Aspect", "Epic string", Category.RENDER, true, false, false);
    }

    public Setting<Double> aspect = register(new Setting<>("Aspect", mc.displayWidth / mc.displayHeight + 0.0, 0.0 ,3.0));

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPerspectiveEvent(PerspectiveEvent event){
        event.setAspect(aspect.getValue().floatValue());
    }
}
