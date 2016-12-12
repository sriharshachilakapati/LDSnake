package com.shc.ldsnake.states;

import com.shc.ldsnake.LDSnake;
import com.shc.ldsnake.Resources;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.SpriteRenderer;
import com.shc.silenceengine.graphics.fonts.BitmapFont;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DynamicProgram;
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
        long snakeHeadTexID = loader.define(Texture.class, FilePath.getResourceFile("snake_head.png"));
        long snakeFoodTexID = loader.define(Texture.class, FilePath.getResourceFile("snake_food.png"));
        long snakeConnTexID = loader.define(Texture.class, FilePath.getResourceFile("snake_connector.png"));
        long backgroundTexID = loader.define(Texture.class, FilePath.getResourceFile("background.png"));

        long fontID = loader.define(BitmapFont.class, FilePath.getResourceFile("engine_resources/fonts/roboto32px.fnt"));

        return new LoadingState(loader, () ->
        {
            Resources.Textures.SNAKE_CELL = loader.get(snakeCellTexID);
            Resources.Textures.SNAKE_HEAD = loader.get(snakeHeadTexID);
            Resources.Textures.SNAKE_FOOD = loader.get(snakeFoodTexID);
            Resources.Textures.SNAKE_CONN = loader.get(snakeConnTexID);
            Resources.Textures.BACKGROUND = loader.get(backgroundTexID);

            Resources.Fonts.NORMAL = loader.get(fontID);

            Resources.Renderers.DYNAMIC = new DynamicRenderer();

            loadDynamicProgram(() -> loadSpriteRenderer(() -> loadBitmapFontRenderer(() ->
                    LDSnake.INSTANCE.setGameState(new PlayState())
            )));
        });
    }

    private static void loadDynamicProgram(SimpleCallback next)
    {
        DynamicProgram.create(dynamicProgram ->
        {
            Resources.Programs.DYNAMIC = dynamicProgram;
            dynamicProgram.applyToRenderer(Resources.Renderers.DYNAMIC);

            next.invoke();
        });
    }

    private static void loadSpriteRenderer(SimpleCallback next)
    {
        SpriteRenderer.create(spriteRenderer ->
        {
            Resources.Renderers.SPRITE = spriteRenderer;
            next.invoke();
        });
    }

    private static void loadBitmapFontRenderer(SimpleCallback next)
    {
        BitmapFontRenderer.create(bitmapFontRenderer ->
        {
            Resources.Renderers.BITMAP_FONT = bitmapFontRenderer;
            next.invoke();
        });
    }
}
