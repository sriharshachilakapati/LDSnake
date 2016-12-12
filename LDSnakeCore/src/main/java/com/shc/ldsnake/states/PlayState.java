package com.shc.ldsnake.states;

import com.shc.ldsnake.Resources;
import com.shc.ldsnake.entities.SnakeCell;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.scene.Scene2D;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    private OrthoCam camera;

    private Scene2D     scene;
    private SpriteBatch batch;

    private GameTimer snakeGrowTimer;

    @Override
    public void onEnter()
    {
        camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

        scene = new Scene2D();
        batch = new SpriteBatch(Resources.Renderers.SPRITE);

        final SnakeCell[] head = new SnakeCell[1];

        scene.entities.add(head[0] = new SnakeCell(100, 100, batch));
        head[0].update(0);

        for (int i = 0; i < 15; i++)
        {
            scene.entities.add(head[0] = new SnakeCell(head[0], batch));
            head[0].update(0);
        }

        snakeGrowTimer = new GameTimer(5, TimeUtils.Unit.SECONDS);

        snakeGrowTimer.setCallback(() ->
        {
            scene.entities.add(head[0] = new SnakeCell(head[0], batch));
            head[0].update(0);
            snakeGrowTimer.start();
        });

        snakeGrowTimer.start();
    }

    @Override
    public void update(float delta)
    {
        scene.update(delta);
    }

    @Override
    public void render(float delta)
    {
        camera.apply();

        DynamicRenderer dynamicRenderer = Resources.Renderers.DYNAMIC;
        Resources.Programs.DYNAMIC.use();

        Resources.Textures.BACKGROUND.bind(0);
        dynamicRenderer.begin(Primitive.TRIANGLE_FAN);
        {
            dynamicRenderer.vertex(0, 0);
            dynamicRenderer.texCoord(0, 0);

            dynamicRenderer.vertex(SilenceEngine.display.getWidth(), 0);
            dynamicRenderer.texCoord(1, 0);

            dynamicRenderer.vertex(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
            dynamicRenderer.texCoord(1, 1);

            dynamicRenderer.vertex(0, SilenceEngine.display.getHeight());
            dynamicRenderer.texCoord(0, 1);
        }
        dynamicRenderer.end();

        batch.begin();
        scene.render(delta);
        batch.end();

        BitmapFontRenderer fontRenderer = Resources.Renderers.BITMAP_FONT;
        fontRenderer.begin();
        {
            fontRenderer.render(Resources.Fonts.NORMAL, "FPS: " + SilenceEngine.gameLoop.getFPS(), 10, 10);
            fontRenderer.render(Resources.Fonts.NORMAL, "\nUPS: " + SilenceEngine.gameLoop.getUPS(), 10, 10);
            fontRenderer.render(Resources.Fonts.NORMAL, "\n\nRC: " + IGraphicsDevice.Data.renderCallsThisFrame, 10, 10);
        }
        fontRenderer.end();
    }

    @Override
    public void resized()
    {
        camera.initProjection(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    @Override
    public void onLeave()
    {
        snakeGrowTimer.stop();
    }
}
