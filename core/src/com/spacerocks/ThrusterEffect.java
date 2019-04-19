package com.spacerocks;

import box2dLight.ConeLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.*;
import com.framework.BaseScreen;

public class ThrusterEffect extends ParticleActor {

    private BodyDef bodyDef;
    private Body body;
    private FixtureDef fixtureDef;

    // private ConeLight light;

    private boolean isOn;

    public ThrusterEffect() {
        super("thruster.pfx", "");

        isOn = false;

        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        body = BaseScreen.world.createBody(bodyDef);

        bodyDef.position.set((getX() + getOriginX()) / 100, (getY() + getOriginY()) / 100);
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(getWidth() / (100 * 2), getHeight() / (100 * 2));
        fixtureDef.shape = rect;
        fixtureDef.isSensor = true;

        Fixture f = body.createFixture(fixtureDef);
        f.setUserData("main");
    }

    // public ConeLight setLight(RayHandler handler) {
    //     light = new ConeLight(handler, 265, Color.SCARLET, 0, 10, 10, 0, 15);
    //     return light;
    // }
    //
    // public ConeLight getLight() {
    //     return light;
    // }
    //
    // public Body getBody() {
    //     return body;
    // }
    //
    // public void setOn() {
    //     isOn = true;
    // }
    //
    // public void setOff() {
    //     isOn = false;
    // }
}
