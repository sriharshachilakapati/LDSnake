package com.shc.ldsnake.states;

import com.shc.ldsnake.Resources;
import com.shc.ldsnake.entities.SnakeCell;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.scene.Scene2D;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    private OrthoCam camera;

    private Scene2D     scene;
    private SpriteBatch batch;

    @Override
    public void onEnter()
    {
        camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

        scene = new Scene2D();
        batch = new SpriteBatch(Resources.Renderers.SPRITE);

        scene.entities.add(new SnakeCell(100, 100, batch));
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
}
