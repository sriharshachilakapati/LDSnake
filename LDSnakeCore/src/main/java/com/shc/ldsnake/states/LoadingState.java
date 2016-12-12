package com.shc.ldsnake.states;

import com.shc.ldsnake.LDSnake;
import com.shc.ldsnake.Resources;
import com.shc.silenceengine.audio.Sound;
import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.graphics.Animation;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.SpriteRenderer;
import com.shc.silenceengine.graphics.SpriteSheet;
import com.shc.silenceengine.graphics.fonts.BitmapFont;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DynamicProgram;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.ResourceLoadingState;
import com.shc.silenceengine.utils.TimeUtils;
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

        long logoTexID = loader.define(Texture.class, FilePath.getResourceFile("logo.png"));
        long snakeCellTexID = loader.define(Texture.class, FilePath.getResourceFile("snake_cell.png"));
        long snakeHeadTexID = loader.define(Texture.class, FilePath.getResourceFile("snake_head.png"));
        long snakeFoodTexID = loader.define(Texture.class, FilePath.getResourceFile("snake_food.png"));
        long snakeConnTexID = loader.define(Texture.class, FilePath.getResourceFile("snake_connector.png"));
        long backgroundTexID = loader.define(Texture.class, FilePath.getResourceFile("background.png"));
        long musicSndID = loader.define(Sound.class, FilePath.getResourceFile("song.ogg"));

        long fontID = loader.define(BitmapFont.class, FilePath.getResourceFile("engine_resources/fonts/roboto32px.fnt"));

        return new LoadingState(loader, () ->
        {
            Resources.Textures.LOGO = loader.get(logoTexID);
            Resources.Textures.SNAKE_CELL = loader.get(snakeCellTexID);
            Resources.Textures.SNAKE_FOOD = loader.get(snakeFoodTexID);
            Resources.Textures.SNAKE_CONN = loader.get(snakeConnTexID);
            Resources.Textures.BACKGROUND = loader.get(backgroundTexID);

            Resources.Sounds.MUSIC = loader.get(musicSndID);
            Resources.Sounds.MUSIC.play(true);

            SpriteSheet snakeHeadSheet = new SpriteSheet(loader.get(snakeHeadTexID), 140, 64);

            Animation snakeHeadAnim = Resources.Animations.SNAKE_HEAD = new Animation();
            snakeHeadAnim.addFrame(snakeHeadSheet.getCell(0, 0), 100, TimeUtils.Unit.MILLIS);
            snakeHeadAnim.addFrame(snakeHeadSheet.getCell(1, 0), 250, TimeUtils.Unit.MILLIS);
            snakeHeadAnim.addFrame(snakeHeadSheet.getCell(0, 0), 100, TimeUtils.Unit.MILLIS);
            snakeHeadAnim.addFrame(snakeHeadSheet.getCell(1, 0), 250, TimeUtils.Unit.MILLIS);
            snakeHeadAnim.addFrame(snakeHeadSheet.getCell(0, 0), 500, TimeUtils.Unit.MILLIS);
            snakeHeadAnim.addFrame(snakeHeadSheet.getCell(1, 0), 250, TimeUtils.Unit.MILLIS);

            Resources.Fonts.NORMAL = loader.get(fontID);

            Resources.Renderers.DYNAMIC = new DynamicRenderer();

            loadDynamicProgram(() -> loadSpriteRenderer(() -> loadBitmapFontRenderer(() ->
                    LDSnake.INSTANCE.setGameState(new LogoState())
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
