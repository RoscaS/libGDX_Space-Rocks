package com.spacerocks;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class LevelScreen extends BaseScreen {

    private Spaceship spaceship;
    private boolean gameOver;

	/*------------------------------------------------------------------*\
	|*							Constructors							*|
	\*------------------------------------------------------------------*/

    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.loadTexture("space.png");
        space.setSize(1920, 1080);
        BaseActor.setWorldBounds(space);

        gameOver = false;

        spaceship = new Spaceship(400, 300, mainStage);

        new Rock(600, 500, mainStage);
        new Rock(600, 300, mainStage);
        new Rock(600, 100, mainStage);
        new Rock(400, 100, mainStage);
        new Rock(200, 100, mainStage);
        new Rock(200, 300, mainStage);
        new Rock(200, 500, mainStage);
        new Rock(400, 500, mainStage);
    }

	/*------------------------------------------------------------------*\
	|*							Public Methods 							*|
	\*------------------------------------------------------------------*/

    public void update(float dt) {
        for (BaseActor rockActor : BaseActor.getList(mainStage, Rock.class.getCanonicalName())) {
            if (rockActor.overlaps(spaceship)) {
                if (spaceship.getShieldPower() <= 0) {
                    Explosion e = new Explosion(0, 0, mainStage);
                    e.centerAtActor(spaceship);
                    spaceship.remove();
                    spaceship.setPosition(-10000, -10000);
                    setEndGame("message-lose.png");
                } else {
                    spaceship.setShieldPower(spaceship.getShieldPower() - 34);
                    Explosion e = new Explosion(0, 0, mainStage);
                    e.centerAtActor(rockActor);
                    rockActor.remove();
                }
            }
            for (BaseActor laserActor : BaseActor.getList(mainStage, Laser.class.getCanonicalName())) {
                if (laserActor.overlaps(rockActor)) {
                    Explosion e = new Explosion(0, 0, mainStage);
                    e.centerAtActor(rockActor);
                    laserActor.remove();
                    rockActor.remove();
                }
            }
        }
        if (!gameOver && BaseActor.count(mainStage, Rock.class.getCanonicalName()) == 0) {
            setEndGame("message-win.png");
        }
    }

	/*------------------------------------------------------------------*\
	|*							Overriden Methods 						*|
	\*------------------------------------------------------------------*/

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.X) spaceship.warp();
        if (keycode == Input.Keys.CONTROL_LEFT) spaceship.shoot();
        return false;
    }

	/*------------------------------------------------------------------*\
	|*							Private Attributs 						*|
	\*------------------------------------------------------------------*/

    private void setEndGame(String textureName) {
        BaseActor messageLose = new BaseActor(0, 0, uiStage);
        messageLose.loadTexture(textureName);
        messageLose.centerAtPosition(1920/2, 1080/2);
        messageLose.setOpacity(0);
        messageLose.addAction(Actions.fadeIn(1));
        gameOver = true;
    }
}
