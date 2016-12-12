package com.shc.ldsnake.entities;

import com.shc.ldsnake.Resources;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.components.SpriteComponent;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.functional.UniCallback;

import static com.shc.silenceengine.input.Keyboard.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class SnakeCell extends Entity2D
{
    private Vector2 velocity;

    private SnakeCell behindCell;
    private Direction direction;

    private UniCallback<Float> updateHandler;

    private SnakeCell()
    {
        velocity = new Vector2();
        direction = Direction.NONE;
    }

    public SnakeCell(float x, float y, SpriteBatch batch)
    {
        this();

        position.set(x, y);
        scale.set(0.5f);

        updateHandler = this::updateHead;

        addComponent(new SpriteComponent(new Sprite(Resources.Textures.SNAKE_CELL), batch));
    }

    public SnakeCell(SnakeCell parent, SpriteBatch batch)
    {
        this();

        updateHandler = this::updateTail;

        behindCell = parent;
        scale.set(0.5f);
        position.set(parent.position);

        switch (parent.direction)
        {
            case NONE:
            case RIGHT:
                position.add(-32, 0);
                break;

            case LEFT:
                position.add(32, 0);
                break;

            case UP:
                position.add(0, 32);
                break;

            case DOWN:
                position.add(0, -32);
                break;
        }

        addComponent(new SpriteComponent(new Sprite(Resources.Textures.SNAKE_CELL), batch));
    }

    @Override
    protected void onUpdate(float deltaTime)
    {
        updateHandler.invoke(deltaTime);
        position.add(velocity);
    }

    private void updateHead(float deltaTime)
    {
        if (Keyboard.isKeyDown(KEY_W))
        {
            velocity.set(0, -4);
            direction = Direction.UP;
        }

        if (Keyboard.isKeyDown(KEY_S))
        {
            velocity.set(0, 4);
            direction = Direction.DOWN;
        }

        if (Keyboard.isKeyDown(KEY_A))
        {
            velocity.set(-4, 0);
            direction = Direction.LEFT;
        }

        if (Keyboard.isKeyDown(KEY_D))
        {
            velocity.set(4, 0);
            direction = Direction.RIGHT;
        }
    }

    private void updateTail(float deltaTime)
    {
        velocity.set(behindCell.position).subtract(position);

        float distToBehind = velocity.length();
        float followDistance = 32;

        if (distToBehind > followDistance)
        {
            float tooFar = distToBehind - followDistance;
            velocity.normalize().scale(tooFar);
        }
        else
            velocity.set(0);

        direction = behindCell.direction;
    }

    private enum Direction
    {
        NONE, UP, DOWN, RIGHT, LEFT
    }
}
