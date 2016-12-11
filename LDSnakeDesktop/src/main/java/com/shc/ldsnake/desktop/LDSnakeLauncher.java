package com.shc.ldsnake.desktop;

import com.shc.silenceengine.backend.lwjgl.LwjglRuntime;
import com.shc.ldsnake.LDSnake;

public class LDSnakeLauncher
{
    public static void main(String[] args)
    {
        LwjglRuntime.start(new LDSnake());
    }
}