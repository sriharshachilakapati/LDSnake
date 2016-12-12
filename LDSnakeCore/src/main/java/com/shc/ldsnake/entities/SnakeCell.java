package com.shc.ldsnake.entities;

import com.shc.ldsnake.Resources;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.scene.components.SpriteComponent;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.functional.UniCallback;

import static com.shc.silenceengine.input.Keyboard.*;

/**
 * @author Sri Harsha Chilakapati
 */
public class SnakeCell extends Entity2D
{
    private Vector2 velocity;

    private SnakeCell prevCell;
    private SnakeCell nextCell;

    private UniCallback<Float> updateHandler;

    private float targetRotation;

    private SnakeCell()
    {
        velocity = new Vector2();
    }

    public SnakeCell(float x, float y, SpriteBatch batch)
    {
        this();

        position.set(x, y);
        scale.set(0.5f);

        updateHandler = this::updateHead;

        addComponent(new SpriteComponent(new Sprite(Resources.Textures.SNAKE_HEAD), batch));
    }

    public SnakeCell(SnakeCell prevCell, SpriteBatch batch)
    {
        this();

        updateHandler = this::updateTail;

        this.prevCell = prevCell;
        prevCell.nextCell = this;

        scale.set(0.5f);
        position.set(prevCell.position);

        addComponent(new SpriteComponent(new Sprite(Resources.Textures.SNAKE_CELL), batch));

        Entity2D connector1 = new Entity2D();
        SpriteComponent connectorComponent1 = new SpriteComponent(new Sprite(Resources.Textures.SNAKE_CONN), batch);
        connectorComponent1.layer = 1;
        connector1.addComponent(connectorComponent1);
        connector1.position.set(-32, 0);
        connector1.scale.set(0.5f);

        Entity2D connector2 = new Entity2D();
        SpriteComponent connectorComponent2 = new SpriteComponent(new Sprite(Resources.Textures.SNAKE_CONN), batch);
        connectorComponent2.layer = 1;
        connector2.addComponent(connectorComponent2);
        connector2.position.set(32, 0);
        connector2.scale.set(0.5f);

        addChild(connector1);
        addChild(connector2);
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
            targetRotation = 270;
        }

        if (Keyboard.isKeyDown(KEY_S))
        {
            velocity.set(0, 4);
            targetRotation = 90;
        }

        if (Keyboard.isKeyDown(KEY_A))
        {
            velocity.set(-4, 0);
            targetRotation = 180;
        }

        if (Keyboard.isKeyDown(KEY_D))
        {
            velocity.set(4, 0);
            targetRotation = 0;
        }

        rotateTo(targetRotation);
    }

    private void updateTail(float deltaTime)
    {
        velocity.set(prevCell.position).subtract(position);

        float distToBehind = velocity.length();
        float followDistance = 32;

        if (prevCell.prevCell == null)
            followDistance = 54;

        if (distToBehind > followDistance)
        {
            float tooFar = distToBehind - followDistance;
            velocity.normalize().scale(tooFar);
        }
        else
            velocity.set(0);

        SnakeCell cell = nextCell == null ? this : nextCell;
        rotation = MathUtils.getDirection(cell.position.x, cell.position.y, prevCell.position.x, prevCell.position.y);
    }

    private void rotateTo(float targetRotation)
    {
        final float clampedTarget = (targetRotation % 360) + (targetRotation < 0 ? 360 : 0);
        final float clampedCurrent = (rotation % 360) + (rotation < 0 ? 360 : 0);

        final float sign = Math.abs(clampedCurrent - clampedTarget) < 180 ? 1 : -1;

        final float speed = Math.min(5, Math.abs(clampedTarget - clampedCurrent));

        if (clampedCurrent < clampedTarget)
            rotation += speed * sign;
        else
            rotation -= speed * sign;
    }
}
