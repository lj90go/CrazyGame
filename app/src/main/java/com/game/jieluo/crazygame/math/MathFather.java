package com.game.jieluo.crazygame.math;

/**
 * Created by edutech on 16/6/22.
 */
public class MathFather implements MathPlay
{
    public String gameName;
    public int gameScore;
    public int gameState;//0:pause 1:play -1:remove
    @Override
    public void playGame() {

    }

    @Override
    public void pauseGame() {

    }

    @Override
    public void removeGame() {

    }

    @Override
    public boolean judgeGame() {
        return true;
    }
}
