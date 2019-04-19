package com.effects;

import com.badlogic.gdx.physics.box2d.*;
import com.framework.ParticleActor;
import com.spacerocks.screens.LevelScreen;

public class ThrusterEffect extends ParticleActor {

    private BodyDef bodyDef;
    private Body body;
    private FixtureDef fixtureDef;


    public ThrusterEffect() {
        super("thruster.pfx", "");

        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        body = LevelScreen.WORLD.createBody(bodyDef);

        bodyDef.position.set((getX() + getOriginX()) / 100, (getY() + getOriginY()) / 100);
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(getWidth() / (100 * 2), getHeight() / (100 * 2));
        fixtureDef.shape = rect;
        fixtureDef.isSensor = true;

        Fixture f = body.createFixture(fixtureDef);
        f.setUserData("main");
    }
}
