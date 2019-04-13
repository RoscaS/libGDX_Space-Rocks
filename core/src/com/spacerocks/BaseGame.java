package com.spacerocks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;

/**
 * Created when program is launched;
 * manages the screens that appear during the game.
 */
public abstract class BaseGame extends Game {

    private static BaseGame game;

    /*------------------------------------------------------------------*\
   	|*							Constructors							*|
   	\*------------------------------------------------------------------*/

    /**
     *  Called when game is initialized; stores global reference to game object.
     */
    public BaseGame() {
        game = this;
    }

    /**
     * Called when game is initialized,
     * after Gdx.input and other objects have been initialized.
     */
    public void create() {
        // prepare for multiple classes/stages/actors to receive discrete input
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);
    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 							*|
   	\*------------------------------------------------------------------*/

    /**
     *  Used to switch screens while game is running.
     *  Method is static to simplify usage.
     */
    public static void setActiveScreen(BaseScreen screen) {
        game.setScreen(screen);
    }
}
