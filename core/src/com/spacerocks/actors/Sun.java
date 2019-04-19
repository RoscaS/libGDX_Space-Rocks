package com.spacerocks.actors;

import box2dLight.ConeLight;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.framework.Box2DActor;
import com.spacerocks.screens.LevelScreen;

public class Sun extends Box2DActor {

    private ConeLight sunlight;

    public final int acceleration = 40;
    public final int maxSpeed = 10;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public Sun(float x, float y, Stage s, World w) {
        super(x, y, s, w);
        loadTexture("rock.png");

        setDynamic();
        setShapeCircle();
        setPhysicsProperties(1, .5f, .1f);
        setMaxSpeed(maxSpeed);
        setAcceleration(acceleration);

        // sunlight
        sunlight = new ConeLight(LevelScreen.HANDLER, 10000, Color.ORANGE, 90, 0, 0, 360, 180);
        sunlight.setSoft(true);
        sunlight.setSoftnessLength(60f);
    }

    @Override
    public void postConstruction() {
        super.postConstruction();
        getBody().setGravityScale(0);
        sunlight.attachToBody(getBody());
    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 							*|
   	\*------------------------------------------------------------------*/

    @Override
	public void act(float dt) {
	    super.act(dt);
    }

    /*------------------------------*\
   	|*				Getters			*|
   	\*------------------------------*/

       public ConeLight getSunlight() {
           return sunlight;
       }
}
