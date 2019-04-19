package com.spacerocks.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.framework.Box2DActor;

public class Sun extends Box2DActor {

    public final int acceleration = 40;
    public final int maxSpeed = 10;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public Sun(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("rock.png");

        setDynamic();
        setShapeCircle();
        setPhysicsProperties(1, .5f, .1f);



        float random = MathUtils.random(30);

        addAction(Actions.forever(Actions.rotateBy(30 + random, 1)));

        setMaxSpeed(50 + random);
        setAcceleration(50 + random);
    }

	public void act(float dt) {
	    super.act(dt);
    }
}
