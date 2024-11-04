package com.gamejam.game.frontend.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.gamejam.game.GameJam;

public class LoadingScreenStage {

    public static Stage initializeUI() {
        Stage loadingScreen = new Stage(new ScreenViewport());

        Stack root = new Stack();
        root.setFillParent(true);

        float tileSize = GameJam.getTileSize();

        Image gameJamLogo = new Image(new Texture(Gdx.files.internal("gamejamlogo.png")));
        gameJamLogo.setSize(tileSize*4, tileSize*4);
        root.add(gameJamLogo);
        root.setSize(tileSize*6, tileSize*6);
        root.setFillParent(false);
        root.setPosition(Gdx.graphics.getWidth()/2 - tileSize*3, Gdx.graphics.getHeight()/2 - tileSize*3);

        loadingScreen.addActor(root);
        return loadingScreen;
    }
}
