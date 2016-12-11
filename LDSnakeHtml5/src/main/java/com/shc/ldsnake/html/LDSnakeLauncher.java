package com.shc.ldsnake.html;

import com.google.gwt.core.client.EntryPoint;
import com.shc.silenceengine.backend.gwt.GwtRuntime;
import com.shc.ldsnake.LDSnake;

public class LDSnakeLauncher implements EntryPoint
{
    @Override
    public void onModuleLoad()
    {
        GwtRuntime.start(new LDSnake());
    }
}