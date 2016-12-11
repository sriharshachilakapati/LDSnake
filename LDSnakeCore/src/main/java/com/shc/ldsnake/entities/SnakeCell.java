package com.shc.ldsnake.entities;

import com.shc.ldsnake.Resources;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.components.SpriteComponent;
import com.shc.silenceengine.scene.entity.Entity2D;

import static com.shc.silenceengine.input.Keyboard.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class SnakeCell extends Entity2D
{
    private Vector2 velocity;

    public SnakeCell(float x, float y, SpriteBatch batch)
    {
        velocity = new Vector2();

        position.set(x, y);
        scale.set(0.5f);

        addComponent(new SpriteComponent(new Sprite(Resources.Textures.SNAKE_CELL), batch));
    }

    @Override
    protected void onUpdate(float deltaTime)
    {
        velocity.set(0);

        if (Keyboard.isKeyDown(KEY_W))
            velocity.y += -4;

        if (Keyboard.isKeyDown(KEY_S))
            velocity.y += 4;

        if (Keyboard.isKeyDown(KEY_A))
            velocity.x += -4;

        if (Keyboard.isKeyDown(KEY_D))
            velocity.x += 4;

        position.add(velocity);
    }
}
