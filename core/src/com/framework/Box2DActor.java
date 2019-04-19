package com.framework;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Box2DActor extends Group {

    // Animation
    private Animation<TextureRegion> animation;
    private boolean animationPaused;
    private float elapsedTime;

    // Physics
    // body definition - used to initialize body
    // body definition data: position, angle,
    //   linearVelocity, angularVelocity,
    //   type (static, dynamic),
    //   fixedRotation (can this object rotate?)
    protected BodyDef bodyDef;
    protected Body body;


    // fixture definition - used to initialize fixture
    // fixture data: shape, density, friction, restituion (0 to 1)
    //  *** weight is calculated via density*area

    // fixture - attached to body
    protected FixtureDef fixtureDef;


    protected Float maxSpeed;   // hundreds of pixels per second
    protected Float maxSpeedX;  // hundreds of pixels per second
    protected Float maxSpeedY;  // hundreds of pixels per second

    protected Float acceleration;
    protected Float deceleration;


    // Stores size of game world for all actors
    private static Rectangle worldBounds;

    // Pixel per metter scale
    public static final int PPM = 100;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public Box2DActor(float x, float y, Stage s) {
        super();

        // perform additional initialization tasks
        setPosition(x, y);
        s.addActor(this);

        // initialize animation data
        animation = null;
        elapsedTime = 0;
        animationPaused = false;

        // initialize physics data
        body = null;
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();

        maxSpeed = null;
        maxSpeedX = null;
        maxSpeedY = null;

        acceleration = 2f;
        deceleration = 2f;
    }

    /*------------------------------------------------------------------*\
   	|*							Static methods							*|
   	\*------------------------------------------------------------------*/

    /*------------------------------*\
   	|*				Tools			*|
   	\*------------------------------*/

    /**
     * Retrieves a list of all instances of the object from the given stage
     * with the given class name or whose class extends the class with the given name.
     * If no instances exist, returns an empty list.
     * Useful when coding interactions between different types of game objects in update method.
     *
     * @param stage     Stage containing BaseActor instances
     * @param className name of a class that extends the BaseActor class
     * @return list of instances of the object in stage which extend with the given class name
     */
    public static ArrayList<BaseActor> getList(Stage stage, String className) {
        ArrayList<BaseActor> actors = new ArrayList<BaseActor>();
        Class theClass = null;
        try {
            theClass = Class.forName(className);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Actor a : stage.getActors()) {
            if (theClass.isInstance(a)) {
                actors.add((BaseActor) a);
            }
        }
        return actors;
    }

    /**
     * Returns number of instances of a given class (that extends BaseActor).
     *
     * @param className name of a class that extends the BaseActor class
     * @return number of instances of the class
     */
    public static int count(Stage stage, String className) {
        return getList(stage, className).size();
    }

    /*------------------------------*\
   	|*				Collision		*|
   	\*------------------------------*/

    /**
     * Set world dimensions for use by methods boundToWorld() and scrollTo().
     *
     * @param width  width of world
     * @param height height of world
     */
    public static void setWorldBounds(float width, float height) {
        worldBounds = new Rectangle(0, 0, width, height);
    }

    /**
     * Set world dimensions for use by methods boundToWorld() and scrollTo().
     *
     * @param ba whose size determines the world bounds (typically a background image)
     */
    public static void setWorldBounds(BaseActor ba) {
        setWorldBounds(ba.getWidth(), ba.getHeight());
    }

    /*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

    /**
     * Uses data to initialize object and add to world.
     */
    public void initializePhysics(World w) {
        // Initialize a body; automatically added to world
        body = w.createBody(bodyDef);

        // Initialize a Fixture and attach it to the body
        Fixture f = body.createFixture(fixtureDef);
        f.setUserData("main");

        // Store reference to this, so can access from collision
        body.setUserData(this);
    }

	/*------------------------------*\
	|*				Getters			*|
	\*------------------------------*/

    public Body getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }

    public Float getMaxSpeed() {
        return maxSpeed;
    }

    public Float getMaxSpeedX() {
        return maxSpeedX;
    }

    public Float getMaxSpeedY() {
        return maxSpeedY;
    }

    public Float getAcceleration() {
        return acceleration;
    }

    public Vector2 getVelocity() {
        return body.getLinearVelocity();
    }

    /**
     * Calculates the magnitude of current velocity.
     *
     * @return speed of movement (pixels/second)
     */
    public float getSpeed() {
        return getVelocity().len();
    }

    /**
     * Get facing angle in degree.
     *
     * @return facing angle.
     */
    public float getRotationAngle() {
        return (float) Math.toDegrees(body.getAngle());
    }

    /**
     * Get the normalized facing direction vector.
     * (cos(rotationAngle), sin(rotationAngle))
     *
     * @return facing direction vector.
     */
    public Vector2 getRotationVector() {
        float angle = body.getAngle();
        float cos = MathUtils.cos(angle);
        float sin = MathUtils.sin(angle);
        return new Vector2(cos, sin);
    }

    /**
     * Determines if this object is moving (if speed is greater than zero).
     *
     * @return false when speed is zero, true otherwise
     */
    public boolean isMoving() {
        return getSpeed() > 0;
    }

	/*------------------------------*\
	|*				Setters			*|
	\*------------------------------*/

    /**
     * Register overlaps; object is not solid.
     */
    public void setSensor() {
        fixtureDef.isSensor = true;
    }

    /**
     * Set maximum speed of this object.
     *
     * @param maxSpeed Maximum speed of this object in (pixels/second).
     */
    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Set maximum speed in X direction of this object.
     *
     * @param maxSpeedX Maximum speed of this object in (pixels/second).
     */
    public void setMaxSpeedX(Float maxSpeedX) {
        this.maxSpeedX = maxSpeedX;
    }

    /**
     * Set maximum speed in XY direction of this object.
     *
     * @param maxSpeedY Maximum speed of this object in (pixels/second).
     */
    public void setMaxSpeedY(Float maxSpeedY) {
        this.maxSpeedY = maxSpeedY;
    }

    /**
     * Set acceleration of this object.
     *
     * @param acc Acceleration in (pixels/second) per second.
     */
    public void setAcceleration(float acc) {
        acceleration = acc;
    }

    /**
     * Set deceleration of this object.
     * Deceleration is only applied when object is not accelerating.
     *
     * @param deceleration Deceleration in (pixels/second) per second.
     */
    public void setDeceleration(float deceleration) {
        this.deceleration = deceleration;
    }

    /**
     * Sets the facing angle (in degrees).
     * If current speed is zero, this will have no effect.
     *
     * @param angle (degrees)
     */
    public void setRotationAngle(float angle) {
        Vector2 v = body.getPosition();
        float deg = (float) Math.toRadians(angle);
        body.setTransform(v.x, v.y, deg);
    }


    public void setVelocity(float vx, float vy) {
        body.setLinearVelocity(vx, vy);
    }

    public void setVelocity(Vector2 v) {
        body.setLinearVelocity(v);
    }

    /**
     * Set the speed of movement (in pixels/second) in current direction.
     * If current speed is zero (direction is undefined),
     * direction will be set to angle of rotation.
     *
     * @param speed of movement (pixels/second)
     */
    public void setSpeed(float speed) {
        if ((int) getSpeed() == 0) {
            setVelocity(getRotationVector().setLength(speed));
        } else {
            setVelocity(getVelocity().setLength(speed));
        }
    }

    public void translateX(float value) {
        Vector2 v = body.getPosition();
        body.setTransform(v.x + value, v.y, 0);
    }

    public void translateY(float value) {
        Vector2 v = body.getPosition();
        body.setTransform(v.x, v.y + value, 0);
    }

    /**
     * Move body to position and keep same rotatation angle.
     * @param position vector
     */
    public void moveTo(Vector2 position) {
        body.setTransform(position, (float) Math.toRadians(getRotationAngle()));
    }

    /**
     * Sets the opacity of this actor.
     *
     * @param opacity value from 0 (transparent) to 1 (opaque)
     */
    public void setOpacity(float opacity) {
        getColor().a = opacity;
    }

    private void setOriginCenter() {
        if (getWidth() == 0) System.err.println("error: actor size not set");
        setOrigin(getWidth() / 2, getHeight() / 2);
    }


    public void setStatic() {
        bodyDef.type = BodyDef.BodyType.StaticBody;
    }

    public void setKinematic() {
        bodyDef.type = BodyDef.BodyType.KinematicBody;
    }

    public void setDynamic() {
        bodyDef.type = BodyDef.BodyType.DynamicBody;
    }

    public void setFixedRotation() {
        bodyDef.fixedRotation = true;
    }

    public void setShapeRectangle() {
        setOriginCenter();
        // position must be centred
        bodyDef.position.set((getX() + getOriginX()) / PPM, (getY() + getOriginY()) / PPM);

        PolygonShape rect = new PolygonShape();
        rect.setAsBox(getWidth() / (PPM * 2), getHeight() / (PPM * 2));
        fixtureDef.shape = rect;
    }

    public void setShapeCircle() {
        setOriginCenter();
        // position must be centred
        bodyDef.position.set((getX() + getOriginX()) / PPM, (getY() + getOriginY()) / PPM);

        CircleShape circ = new CircleShape();
        circ.setRadius(getWidth() / (PPM * 2));
        fixtureDef.shape = circ;
    }

    public void setPhysicsProperties(float density, float friction, float restitution) {
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
    }

    /*------------------------------*\
   	|*				Tools   		*|
   	\*------------------------------*/

    public void applyForce(Vector2 force) {
        body.applyForceToCenter(force, true);
    }

    public void applyImpluse(Vector2 impulse) {
        body.applyLinearImpulse(impulse, body.getPosition(), true);
    }

    /**
     * Update accelerate vector by current rotation angle and value stored in acceleration field.
     * Acceleration is applied by <code>applyPhysics</code> method.
     *
     * @see #acceleration
     */
    public void moveForward() {
        applyForce(getRotationVector().setLength(acceleration));
    }

    /**
     * Align center of actor at given position coordinates.
     *
     * @param x x-coordinate to center at
     * @param y y-coordinate to center at
     */
    public void centerAtPosition(float x, float y) {
        body.setTransform(x / PPM, y / PPM, body.getAngle());
    }

    /*------------------------------*\
   	|*				Overriden		*|
   	\*------------------------------*/


    /**
     * Processes all Actions and related code for this object;
     * automatically called by act method in Stage class.
     * <p>
     * Here Act method serves two purposes. First, it will adjust the velocity of the
     * body if it exceeds any of the set maximum values. Second, it will update the actor properties (position and
     * angle) based on the properties of the body. In this process, physics units must be scaled back to pixel units,
     * and the angle of rotation must be converted from radians (used by the body) to degrees (used by the actor).
     *
     * @param dt elapsed time (second) since last frame (supplied by Stage act method)
     */
    @Override
    public void act(float dt) {
        super.act(dt);

        if (animationPaused) return;
        elapsedTime += dt;


        // Cap max speeds, if it's set.
        if (maxSpeedX != null) {
            Vector2 v = getVelocity();
            v.x = MathUtils.clamp(v.x, -maxSpeedX, maxSpeedX);
            setVelocity(v);
        }
        if (maxSpeedY != null) {
            Vector2 v = getVelocity();
            v.x = MathUtils.clamp(v.y, -maxSpeedY, maxSpeedY);
            setVelocity(v);
        }
        if (maxSpeed != null) {
            float s = getSpeed();
            if (s > maxSpeed)
                setSpeed(maxSpeed);
        }

        // update image data (position and rotation) based on physics data
        Vector2 center = body.getWorldCenter();
        setPosition(100 * center.x - getOriginX(), 100 * center.y - getOriginY());

        setRotation(getRotationAngle());
        // float a = body.getAngle(); // angle in radians
        // setRotation((float) (a * 360 / 2 * Math.PI));
    }

    /**
     * Draws current frame of animation; automatically called by draw method in Stage class. <br>
     * If color has been set, image will be tinted by that color. <br>
     * If no animation has been set or object is invisible, nothing will be drawn.
     *
     * @param batch       (supplied by Stage draw method)
     * @param parentAlpha (supplied by Stage draw method)
     * @see #setColor
     * @see #setVisible
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {

        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);

        if (animation != null && isVisible()) {
            batch.draw(animation.getKeyFrame(elapsedTime),
                    getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(),
                    getScaleY(), getRotation()
            );
        }
        super.draw(batch, parentAlpha);
    }

    /*------------------------------*\
   	|*				Animation		*|
   	\*------------------------------*/

    /**
     * Sets the animation used when rendering this actor; also sets actor size.
     *
     * @param anim animation that will be drawn when actor is rendered
     */
    public void setAnimation(Animation<TextureRegion> anim) {
        animation = anim;
        TextureRegion tr = animation.getKeyFrame(0);
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        setSize(w, h);
        setOrigin(w / 2, h / 2);
    }

    /**
     * Set the pause state of the animation.
     *
     * @param pause true to pause animation, false to resume animation
     */
    public void setAnimationPaused(boolean pause) {
        animationPaused = pause;
    }


    /**
     * Checks if animation is complete: if play mode is normal (not looping)
     * and elapsed time is greater than time corresponding to last frame.
     */
    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(elapsedTime);
    }

    /**
     * Creates an animation from images stored in separate files.
     *
     * @param fileNames     array of names of files containing animation images
     * @param frameDuration how long each frame should be displayed
     * @param loop          should the animation loop
     * @return animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames, float frameDuration,
                                                           boolean loop) {

        Array<TextureRegion> textureArray = new Array<TextureRegion>();

        for (String name : fileNames) {
            Texture texture = new Texture(Gdx.files.internal(name));
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            textureArray.add(new TextureRegion(texture));
        }

        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
        anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);

        if (animation == null) {
            setAnimation(anim);
        }
        return anim;
    }

    /**
     * Creates an animation from a spritesheet: a rectangular grid of images stored in a single file.
     *
     * @param fileName      name of file containing spritesheet
     * @param rows          number of rows of images in spritesheet
     * @param cols          number of columns of images in spritesheet
     * @param frameDuration how long each frame should be displayed
     * @param loop          should the animation loop
     * @return animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols,
                                                           float frameDuration, boolean loop) {

        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<TextureRegion>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                textureArray.add(temp[r][c]);
            }
        }

        Animation<TextureRegion> anim = new Animation<TextureRegion>(frameDuration, textureArray);
        anim.setPlayMode(loop ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL);

        if (animation == null) {
            setAnimation(anim);
        }
        return anim;
    }

    /**
     * Convenience method for creating a 1-frame animation from a single texture.
     *
     * @param fileName names of image file
     * @return animation created (useful for storing multiple animations)
     */
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return loadAnimationFromFiles(fileNames, 1, true);
    }

    /*------------------------------*\
   	|*				Physics 		*|
   	\*------------------------------*/

    // /**
    //  * Adjust velocity vector based on acceleration vector,
    //  * then adjust position based on velocity vector. <br>
    //  * If not accelerating, deceleration value is applied. <br>
    //  * Speed is limited by maxSpeed value. <br>
    //  * Acceleration vector reset to (0,0) at end of method. <br>
    //  *
    //  * @param dt Time elapsed since previous frame (delta time); typically obtained from <code>act</code> method.
    //  * @see #acceleration
    //  * @see #deceleration
    //  * @see #maxSpeed
    //  */
    // public void applyPhysics(float dt) {
    //     // apply acceleration
    //     velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);
    //     float speed = getSpeed();
    //     // decelerate when not accelerating
    //     if (accelerationVec.len() == 0) {
    //         speed -= deceleration * dt;
    //     }
    //     // keep speed within set bounds
    //     speed = MathUtils.clamp(speed, 0, maxSpeed);
    //     // update velocity
    //     setSpeed(speed);
    //     // apply velocity
    //     moveBy(velocityVec.x * dt, velocityVec.y * dt);
    //     // reset acceleration
    //     accelerationVec.set(0, 0);
    // }

    /*------------------------------*\
   	|*				Collision  		*|
   	\*------------------------------*/

    /**
     * If an edge of an object moves past the world bounds,
     * adjust its position to keep it completely on screen.
     */
    public void boundToWorld() {
        if (getX() < 0) setX(0);
        if (getX() + getWidth() > worldBounds.width) setX(worldBounds.width - getWidth());
        if (getY() < 0) setY(0);
        if (getY() + getHeight() > worldBounds.height) setY(worldBounds.height - getHeight());
    }

    /*------------------------------*\
   	|*				Camera  		*|
   	\*------------------------------*/

    /**
     * Center camera on this object, while keeping camera's range of view
     * (determined by screen size) completely within world bounds.
     */
    public void alignCamera() {
        Camera cam = this.getStage().getCamera();
        Viewport v = this.getStage().getViewport();

        // center camera on actor
        cam.position.set(getX() + getOriginX(), getY() + getOriginY(), 0);

        // bound camera to layout
        cam.position.x = MathUtils.clamp(
                cam.position.x, cam.viewportWidth / 2,
                worldBounds.width - cam.viewportWidth / 2
        );
        cam.position.y = MathUtils.clamp(
                cam.position.y, cam.viewportHeight / 2,
                worldBounds.height - cam.viewportHeight / 2
        );
    }

    /*------------------------------*\
   	|*				Behave  		*|
   	\*------------------------------*/

    /**
     * If this object moves completely past the world bounds,
     * adjust its position to the opposite side of the world.
     */
    public void wrapAroundWorld() {
        if (getX() + getWidth() < 0) {
            centerAtPosition(worldBounds.width, getY());
        }
        if (getX() > worldBounds.width) {
            centerAtPosition(0, getY());
        }
        if (getY() + getHeight() < 0) {
            centerAtPosition(getX(), worldBounds.height);
        }
        if (getY() > worldBounds.height) {
            centerAtPosition(getX(), 0);
        }
    }
}
