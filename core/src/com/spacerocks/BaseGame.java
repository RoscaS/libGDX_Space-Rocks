package com.spacerocks;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created when program is launched;
 * manages the screens that appear during the game.
 */
public abstract class BaseGame extends Game {

    /**
     * Stores reference to game; used when calling setActiveScreen method.
     */
    private static BaseGame game;

    public static Label.LabelStyle labelStyle; // BitmapFont + Color

    /*------------------------------------------------------------------*\
   	|*							Constructors							*|
   	\*------------------------------------------------------------------*/

    /**
     * Called when game is initialized; stores global reference to game object.
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

        fontSetup();
    }

    /*------------------------------------------------------------------*\
   	|*							Public Methods 							*|
   	\*------------------------------------------------------------------*/

    /**
     * Used to switch screens while game is running.
     * Method is static to simplify usage.
     */
    public static void setActiveScreen(BaseScreen screen) {
        game.setScreen(screen);
    }

    /*------------------------------------------------------------------*\
   	|*							Private Methods							*|
   	\*------------------------------------------------------------------*/

    /**
     * Parameters for generating a custom bitmap font.
     */
    private void fontSetup() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto.ttf"));
        FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
        fontParameters.size = 48;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 2;
        fontParameters.borderColor = Color.BLACK;
        fontParameters.minFilter = Texture.TextureFilter.Linear;
        fontParameters.magFilter = Texture.TextureFilter.Linear;

        BitmapFont customFont = fontGenerator.generateFont(fontParameters);

        labelStyle = new Label.LabelStyle();
        labelStyle.font = customFont;
    }
}
