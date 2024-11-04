package com.gamejam.game.frontend.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gamejam.game.GameJam;

public class LoadingScreenStage extends Stage{

    private float loadingElapsed = 0;
    private boolean endLoadingScreen = false;
    private Sound loadingSound = Gdx.audio.newSound(Gdx.files.internal("loadingScreenJingle.mp3"));
    private Stack root;

    public LoadingScreenStage() {

        Gdx.app.log("LoadingScreen", "LoadingScreen initializing");

        root = new Stack();
        root.setFillParent(true);

        float tileSize = GameJam.getTileSize();

        Gdx.app.log("LoadingScreen", "Loading gamejamlogo");
        Image gameJamLogo = new Image(new Texture(Gdx.files.internal("gamejamlogo.png")));
        gameJamLogo.setSize(tileSize*4, tileSize*4);
        root.add(gameJamLogo);

        root.setSize(tileSize*6, tileSize*6);
        root.setFillParent(false);
        root.setPosition((float) Gdx.graphics.getWidth() /2 - tileSize*3, (float) Gdx.graphics.getHeight() /2 - tileSize*3);

        addActor(root);

        // play the loading screen jingle
        playLoadingScreenJingle();

        Gdx.app.log("LoadingScreen", "LoadingScreen initialized");
    }

    public void endLoadingScreen(){
        endLoadingScreen = true;
    }

    // draw the stage
    @Override
    public void draw() {
        super.draw();
        root.draw(getBatch(), 1);
    }

    // act the stage
    @Override
    public void act() {
        super.act();
        root.act(Gdx.graphics.getDeltaTime());
        loadingElapsed += Gdx.graphics.getDeltaTime();
        // run loading screen for 4 seconds atleast
        if(loadingElapsed >= 4 & endLoadingScreen){
            // ensures game starts in menu
            GameJam.createMainMenuStage();
            Gdx.app.log("LoadingScreen", "LoadingScreen finished");
            clear();
            dispose();
        }
        loadingElapsed += Gdx.graphics.getDeltaTime();
    }

    public void playLoadingScreenJingle() {
        /*
         * method for playing the loading screen jingle
         */
        // check to see if loadingSound is not null
        if (loadingSound == null) {
            Gdx.app.log("LoadingStage", "Loading Screen Jingle not played, loadingSound is null");
            return;
        }
        Gdx.app.log("GameJam", "Loading Screen Jingle played");
        loadingSound.play(GameJam.getVolume());
    }
}

