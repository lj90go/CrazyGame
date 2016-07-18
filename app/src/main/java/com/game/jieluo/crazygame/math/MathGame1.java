package com.game.jieluo.crazygame.math;

import android.util.Log;

/**
 * Created by edutech on 16/6/22.
 */
public class MathGame1 extends MathFather
{

    private int lineNumber = 3;
    private int allNumber = 10;
    private int maxRemoveNumber = 2;
    private int nextNumber = 0;
    private int removedNumber = 0;
    private int remainNumber = 0;
    public MathGame1(String gameName, int score)
    {
        this.gameName = gameName;
        this.gameScore = score;
    }
    public void setInitParams(int lineNumber, int allNumber, int maxRemoveNumber)
    {
        this.lineNumber = lineNumber;
        this.allNumber = allNumber;
        this.maxRemoveNumber = maxRemoveNumber;
        this.remainNumber = allNumber;
    }

    public int getNextNumber() {
        return nextNumber;
    }
    public void remove(int number)
    {
        remainNumber = remainNumber - number;
        removedNumber = number;
    }

    public int getRemainNumber() {
        return remainNumber;
    }

    @Override
    public void playGame() {
        this.gameState = 1;
        int number = allNumber%(maxRemoveNumber+1);
        nextNumber = number;
        NSLog.DLog(this.gameName,"playGame:"+nextNumber);
    }

    @Override
    public void pauseGame() {
        this.gameState = 0;
        NSLog.DLog(this.gameName,"pauseGame");
    }

    @Override
    public void removeGame() {
        this.gameState = -1;
        this.removedNumber = 0;
        this.nextNumber = 0;
        this.maxRemoveNumber = 2;
        this.allNumber = 10;
        this.lineNumber = 3;
        NSLog.DLog(this.gameName,"removeGame");
    }

    @Override
    public boolean judgeGame() {
        NSLog.DLog(this.gameName,"judgeGame");
        if(allNumber/maxRemoveNumber<3)
        {
            NSLog.DLog(this.gameName,"the totalnumber/maxRemoveNumber must >2");
            return false;
        }
        if(allNumber>remainNumber&&remainNumber<=0)
        {
            NSLog.DLog(this.gameName,"game over");
            return false;
        }
        if(removedNumber>maxRemoveNumber)
        {
            NSLog.DLog(this.gameName,"the removednumber must less than "+maxRemoveNumber);
            return false;
        }else
        {
            nextNumber = maxRemoveNumber - removedNumber + 1;
        }
        return true;

    }

}
