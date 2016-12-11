package com.shc.ldsnake.states;

import com.shc.ldsnake.LDSnake;
import com.shc.ldsnake.Resources;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.ResourceLoadingState;
import com.shc.silenceengine.utils.functional.SimpleCallback;

/**
 * @author Sri Harsha Chilakapati
 */
public class LoadingState extends ResourceLoadingState
{
    private LoadingState(ResourceLoader loader, SimpleCallback doneCallback)
    {
        super(loader, doneCallback);
    }

    public static LoadingState create()
    {
        ResourceLoader loader = new ResourceLoader();

        long snakeCellTexID = loader.define(Texture.class, FilePath.getResourceFile("snake_cell.png"));

        return new LoadingState(loader, () ->
        {
            Resources.Textures.SNAKE_CELL = loader.get(snakeCellTexID);
            LDSnake.INSTANCE.setGameState(new PlayState());
        });
    }
}
