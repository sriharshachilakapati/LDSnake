package com.shc.ldsnake;

import com.shc.ldsnake.states.LoadingState;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.input.Keyboard;

public class LDSnake extends Game
{
    public static LDSnake INSTANCE;

    @Override
    public void init()
    {
        INSTANCE = this;

        SilenceEngine.display.setTitle("LDSnake: Game for LudumDare 37");
        SilenceEngine.display.setSize(1280, 720);
        SilenceEngine.display.centerOnScreen();

        setGameState(LoadingState.create());
    }

    @Override
    public void update(float deltaTime)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_ESCAPE))
            SilenceEngine.display.close();
    }
}