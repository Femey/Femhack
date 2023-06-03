package me.Femhack.features.modules.render;

import me.Femhack.event.events.RenderItemEvent;
import me.Femhack.features.modules.Module;
import me.Femhack.features.setting.Setting;
import me.Femhack.util.Timer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ViewModel extends Module
{
    private static ViewModel INSTANCE;
    public Setting<Settings> settings = this.register(new Setting("Settings", Settings.TRANSLATE));

    public Setting<Boolean> useAlpha = this.register(new Setting("Use Opacity", false, v -> this.settings.getValue() == Settings.TWEAKS));
    public Setting<Integer> alpha = this.register(new Setting("Opacity", 255, 0, 255, v -> this.settings.getValue() == Settings.TWEAKS && useAlpha.getValue()));
    public Setting<Boolean> glint = this.register(new Setting("Glint", true, v -> this.settings.getValue() == Settings.TWEAKS));
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
    public Setting<Boolean> animationY = this.register(new Setting("AnimationY", false, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> delayY = this.register(new Setting("Delay Y", 0, 0, 100, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Boolean> animationX = this.register(new Setting("AnimationX", false, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> delayX = this.register(new Setting("Delay X", 0, 0, 100, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Boolean> animationZ = this.register(new Setting("AnimationZ", false, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Integer> delayZ = this.register(new Setting("Delay Z", 0, 0, 100, v -> this.settings.getValue() == Settings.ROTATE));
    public Setting<Double> offScaleX = this.register(new Setting("OffScaleX", 1.0, 0.1, 5.0, v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleY = this.register(new Setting("OffScaleY", 1.0, 0.1, 5.0, v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> offScaleZ = this.register(new Setting("OffScaleZ", 1.0, 0.1, 5.0, v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> mainScaleX = this.register(new Setting("MainScaleX", 1.0, 0.1, 5.0, v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> mainScaleY = this.register(new Setting("MainScaleY", 1.0, 0.1, 5.0, v -> this.settings.getValue() == Settings.SCALE));
    public Setting<Double> mainScaleZ = this.register(new Setting("MainScaleZ", 1.0, 0.1, 5.0, v -> this.settings.getValue() == Settings.SCALE));


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

    int roty = 0;
    int offroty = 0;
    int rotx = 0;
    int offrotx = 0;
    int rotz = 0;
    int offrotz = 0;
    Timer timer = new Timer();
    Timer timer2 = new Timer();
    Timer timer3 = new Timer();

    @SubscribeEvent
    public void onItemRender(final RenderItemEvent event) {
        if (animationY.getValue() && timer.passedMs(delayY.getValue())){
            if (roty >= 36){
                roty = -36;
            }
            roty += 1;
            if (offroty <= -36){
                offroty = 36;
            }
            offroty -= 1;
            mainRotY.setValue(roty);
            offRotY.setValue(offroty);
            timer.reset();
        }
        if (animationX.getValue() && timer2.passedMs(delayX.getValue())){
            if (rotx >= 36){
                rotx = -36;
            }
            rotx += 1;
            if (offrotx <= -36){
                offrotx = 36;
            }
            offrotx -= 1;
            mainRotX.setValue(rotx);
            offRotX.setValue(offrotx);
            timer2.reset();
        }
        if (animationZ.getValue() && timer3.passedMs(delayZ.getValue())){
            if (rotz >= 36){
                rotz = -36;
            }
            rotz += 1;
            if (offrotz <= -36){
                offrotz = 36;
            }
            offrotz -= 1;
            mainRotZ.setValue(rotz);
            offRotZ.setValue(offrotz);
            timer3.reset();
        }
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
        event.setMainHandScaleX(this.mainScaleX.getValue());
        event.setMainHandScaleY(this.mainScaleY.getValue());
        event.setMainHandScaleZ(this.mainScaleZ.getValue());
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

