package com.shc.ldsnake.states;

import com.shc.ldsnake.Resources;
import com.shc.ldsnake.entities.SnakeCell;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
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
        snakeGrowTimer = new GameTimer(5, TimeUtils.Unit.SECONDS);

        snakeGrowTimer.setCallback(() ->
        {
            scene.entities.add(head[0] = new SnakeCell(head[0], batch));
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
        batch.begin();
        scene.render(delta);
        batch.end();
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
