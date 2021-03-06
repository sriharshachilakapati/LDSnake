package com.shc.ldsnake.states;

import com.shc.ldsnake.RenderUtils;
import com.shc.ldsnake.Resources;
import com.shc.ldsnake.entities.SnakeCell;
import com.shc.ldsnake.entities.SnakeFood;
import com.shc.silenceengine.collision.broadphase.DynamicTree2D;
import com.shc.silenceengine.collision.colliders.SceneCollider2D;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.scene.Scene2D;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    public static final List<Entity2D> DEAD = new ArrayList<>();
    public static final List<Entity2D> NEW  = new ArrayList<>();

    public static SpriteBatch batch;

    static float score;

    private OrthoCam  camera;
    private Scene2D   scene;
    private GameTimer snakeFoodTimer;

    private SceneCollider2D collider;

    @Override
    public void onEnter()
    {
        NEW.clear();
        DEAD.clear();

        score = 0;

        camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

        scene = new Scene2D();

        collider = new SceneCollider2D(new DynamicTree2D());
        collider.register(Resources.CollisionTags.SNAKE_HEAD, Resources.CollisionTags.SNAKE_CELL);
        collider.register(Resources.CollisionTags.SNAKE_HEAD, Resources.CollisionTags.SNAKE_FOOD);
        collider.setScene(scene);

        batch = new SpriteBatch(Resources.Renderers.SPRITE);

        SnakeCell head = new SnakeCell(100, 100, batch);
        scene.entities.add(head);
        scene.entities.add(new SnakeCell(head, batch));

        snakeFoodTimer = new GameTimer(5, TimeUtils.Unit.SECONDS);

        snakeFoodTimer.setCallback(() ->
        {
            Entity2D entity = new SnakeFood(batch);
            NEW.add(entity);

            snakeFoodTimer.start();
        });

        snakeFoodTimer.start();
    }

    @Override
    public void update(float delta)
    {
        scene.entities.removeAll(DEAD);
        scene.entities.addAll(NEW);

        DEAD.clear();
        NEW.clear();

        scene.update(delta);
        collider.checkCollisions();

        score += delta * SnakeCell.getLength();
    }

    @Override
    public void render(float delta)
    {
        camera.apply();

        RenderUtils.renderTexture(Resources.Textures.BACKGROUND, 0, 0,
                SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

        batch.begin();
        scene.render(delta);
        batch.end();

        BitmapFontRenderer fontRenderer = Resources.Renderers.BITMAP_FONT;
        fontRenderer.begin();
        {
            fontRenderer.render(Resources.Fonts.NORMAL, "FPS: " + SilenceEngine.gameLoop.getFPS(), 10, 10);
            fontRenderer.render(Resources.Fonts.NORMAL, "\nUPS: " + SilenceEngine.gameLoop.getUPS(), 10, 10);
            fontRenderer.render(Resources.Fonts.NORMAL, "\n\nRC: " + IGraphicsDevice.Data.renderCallsThisFrame, 10, 10);

            String message = "You scored " + (int) PlayState.score;
            float messageX = SilenceEngine.display.getWidth() / 2 - Resources.Fonts.NORMAL.getWidth(message) / 2;
            float messageY = 10;

            fontRenderer.render(Resources.Fonts.NORMAL, message, messageX, messageY);
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
        snakeFoodTimer.stop();
    }
}
