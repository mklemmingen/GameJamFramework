package com.gamejam.game.frontend.actors;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class ThreeSecondsSelfDestruct extends Actor {
    /*
    This class takes an Image, a Table or a Stack and adds it as its Image.
    This class self destructs 3 seconds after its first time being acted on (not drawn!)
     */

    Stack selfImage = new Stack();
    float timeElapsed = 0;

    public ThreeSecondsSelfDestruct(Image image){
        selfImage.add(image);
    }
    public ThreeSecondsSelfDestruct(Table table){
        selfImage.add(table);
    }
    public ThreeSecondsSelfDestruct(Stack stack){
        selfImage.add(stack);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        timeElapsed += delta;
        if (timeElapsed > 3){
            selfImage.remove();
        }
    }

    public void setSelfVisage(Stack stack){
        selfImage = stack;
    }

    public Stack getSelfVisage(){
        return selfImage;
    }
}
