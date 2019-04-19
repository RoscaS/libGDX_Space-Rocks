package com.spacerocks.actors;

import box2dLight.PointLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.framework.Box2DActor;
import com.spacerocks.Rock;
import com.spacerocks.screens.LevelScreen;

public class Laser2 extends Box2DActor {

    public final int acceleration = 40;
    public final int maxSpeed = 40;

    public PointLight light;
    public boolean contact;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public Laser2(float x, float y, Stage s) {
        super(x, y, s);

        contact = false;

        loadTexture("laser.png");

        addAction(Actions.delay(.25f));
        addAction(Actions.after(Actions.fadeOut(.2f)));
        addAction(Actions.after(Actions.removeActor()));

        setDynamic();
        setShapeRectangle();
        setPhysicsProperties(.5f, 100f, 0);
        setMaxSpeed(maxSpeed);
        setAcceleration(acceleration);

        light = new PointLight(LevelScreen.HANDLER, 265, Color.GREEN, .9f, 0, 0);
        light.setXray(true);
        light.update();

        LevelScreen.WORLD.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                if (Box2DActor.isContactBetween(contact, Laser2.class, Rock.class)) {
                    Laser2.this.contact = true;
                    Body bodyB = contact.getFixtureB().getBody();
                    LevelScreen.explosion((Box2DActor)bodyB.getUserData());
                    light.attachToBody(bodyB);
                    light.setDistance(4f);
                    addAction(Actions.fadeOut(.01f));
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });

    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 							*|
   	\*------------------------------------------------------------------*/

    @Override
    public void act(float dt) {
        if (contact) {
            try {
                light.setColor(Color.RED);
                light.setDistance(light.getDistance() - dt * 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.act(dt);
    }

    @Override
    public boolean remove() {
        contact = false;
        try {
            light.remove();
            LevelScreen.WORLD.destroyBody(body);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void explosion(Rock rock) {
        // ExplosionEffect boom = new ExplosionEffect();
        // boom.centerAtActor(rock);
        // boom.start();
        // getStage().addActor(boom);
    }
}
