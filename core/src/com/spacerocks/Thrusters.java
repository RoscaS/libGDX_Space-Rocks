package com.spacerocks;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.framework.BaseActor;

public class Thrusters extends BaseActor {

    public Thrusters(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("fire.png");
    }

    public Thrusters(float x, float y, Stage s, boolean gaz) {
        super(x, y, s);
        loadTexture("gaz.png");
    }
}
