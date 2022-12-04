package me.Femhack.features.modules.render;

import me.Femhack.event.events.RenderItemEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel extends Module
{
    private static ViewModel INSTANCE;
    public Setting<Settings> settings = this.register(new Setting("Settings", Settings.TRANSLATE));
    public Setting<Boolean> noEatAnimation = this.register(new Setting("NoEatAnimation", false, v -> this.settings.getValue() == Settings.TWEAKS));
    public Setting<Double> eatX = this.register(new Setting("EatX", 1.0, (-2.0), 5.0, v -> this.settings.getValue() == Settings.TWEAKS && !this.noEatAnimation.getValue()));
    public Setting<Double> eatY = this.register(new Setting("EatY", 1.0, (-2.0), 5.0, v -> this.settings.getValue() == Settings.TWEAKS && !this.noEatAnimation.getValue()));
    public Setting<Boolean> doBob = this.register(new Setting("ItemBob", true, v -> this.settings.getValue() == Settings.TWEAKS));
    public Setting<Double> mainX = this.register(new Setting("MainX", 1.2, (-2.0), 4.0, v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> mainY = this.register(new Setting("MainY", (-0.95), (-3.0), 3.0, v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> mainZ = this.register(new Setting("MainZ", (-1.45), (-5.0), 5.0, v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offX = this.register(new Setting("OffX", 1.2, (-2.0), 4.0, v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offY = this.register(new Setting("OffY", (-0.95), (-3.0), 3.0, v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Double> offZ = this.register(new Setting("OffZ", (-1.45), (-5.0), 5.0, v -> this.settings.getValue() == Settings.TRANSLATE));
    public Setting<Integer> mainRotX = this.register(new Setting("MainRotationX", 0, (-36), 36, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> mainRotY = this.register(new Setting("MainRotationY", 0, (-36), 36, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> mainRotZ = this.register(new Setting("MainRotationZ", 0, (-36), 36, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> offRotX = this.register(new Setting("OffRotationX", 0, (-36), 36, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> offRotY = this.register(new Setting("OffRotationY", 0, (-36), 36, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> offRotZ = this.register(new Setting("OffRotationZ", 0, (-36), 36, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Double> offScaleX = this.register(new Setting("OffScaleX", 1.0, 0.1, 5.0, v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleY = this.register(new Setting("OffScaleY", 1.0, 0.1, 5.0, v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleZ = this.register(new Setting("OffScaleZ", 1.0, 0.1, 5.0, v -> this.settings.getValue() == Settings.SCALE));

    public ViewModel() {
        super("ItemModel", "Cool", Category.RENDER, true, false, false);
        this.setInstance();
    }

    public static ViewModel getInstance() {
        if (ViewModel.INSTANCE == null) {
            ViewModel.INSTANCE = new ViewModel();
        }
        return ViewModel.INSTANCE;
    }

    private void setInstance() {
        ViewModel.INSTANCE = this;
    }

    @SubscribeEvent
    public void onItemRender(final RenderItemEvent event) {
        event.setMainX(this.mainX.getValue());
        event.setMainY(this.mainY.getValue());
        event.setMainZ(this.mainZ.getValue());
        event.setOffX(-this.offX.getValue());
        event.setOffY(this.offY.getValue());
        event.setOffZ(this.offZ.getValue());
        event.setMainRotX(this.mainRotX.getValue() * 5);
        event.setMainRotY(this.mainRotY.getValue() * 5);
        event.setMainRotZ(this.mainRotZ.getValue() * 5);
        event.setOffRotX(this.offRotX.getValue() * 5);
        event.setOffRotY(this.offRotY.getValue() * 5);
        event.setOffRotZ(this.offRotZ.getValue() * 5);
        event.setOffHandScaleX(this.offScaleX.getValue());
        event.setOffHandScaleY(this.offScaleY.getValue());
        event.setOffHandScaleZ(this.offScaleZ.getValue());
    }

    static {
        ViewModel.INSTANCE = new ViewModel();
    }

    private enum Settings
    {
        TRANSLATE,
        ROTATE,
        SCALE,
        TWEAKS;
    }
}

