package com.shc.ldsnake.entities;

import com.shc.ldsnake.LDSnake;
import com.shc.ldsnake.Resources;
import com.shc.ldsnake.states.LogoState;
import com.shc.ldsnake.states.PlayState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector2;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
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
    private static SnakeCell head;

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
        head = this;

        position.set(x, y);
        scale.set(0.5f);

        updateHandler = this::updateHead;

        SpriteComponent spriteComponent = new SpriteComponent(new Sprite(Resources.Animations.SNAKE_HEAD), batch);
        spriteComponent.sprite.setEndCallback(spriteComponent.sprite::start);
        spriteComponent.sprite.start();

        addComponent(spriteComponent);

        Polygon polygon = new Rectangle(124, 64).createPolygon();
        addComponent(new CollisionComponent2D(Resources.CollisionTags.SNAKE_HEAD, polygon, this::headTailCollision));
    }

    public SnakeCell(SnakeCell prevCell, SpriteBatch batch)
    {
        this();

        updateHandler = this::updateTail;

        this.prevCell = prevCell;
        prevCell.nextCell = this;

        scale.set(0.5f);
        position.set(prevCell.position);

        SpriteComponent spriteComponent = new SpriteComponent(new Sprite(Resources.Textures.SNAKE_CELL), batch);
        spriteComponent.layer = 1;

        addComponent(spriteComponent);

        Entity2D connector1 = new Entity2D();
        SpriteComponent connectorComponent1 = new SpriteComponent(new Sprite(Resources.Textures.SNAKE_CONN), batch);
        connectorComponent1.layer = 2;
        connector1.addComponent(connectorComponent1);
        connector1.position.set(-32, 0);
        connector1.scale.set(0.5f);

        Entity2D connector2 = new Entity2D();
        SpriteComponent connectorComponent2 = new SpriteComponent(new Sprite(Resources.Textures.SNAKE_CONN), batch);
        connectorComponent2.layer = 2;
        connector2.addComponent(connectorComponent2);
        connector2.position.set(32, 0);
        connector2.scale.set(0.5f);

        addChild(connector1);
        addChild(connector2);

        Polygon polygon = new Rectangle(63, 43).createPolygon();
        addComponent(new CollisionComponent2D(Resources.CollisionTags.SNAKE_CELL, polygon));
    }

    public static int getLength()
    {
        int count = 0;

        SnakeCell cell = head;

        while ((cell = cell.nextCell) != null)
            count++;

        return count;
    }

    @Override
    protected void onUpdate(float deltaTime)
    {
        updateHandler.invoke(deltaTime);
        position.add(velocity);
    }

    private void updateHead(float deltaTime)
    {
        if (Keyboard.isKeyDown(KEY_W) || Keyboard.isKeyDown(KEY_UP))
        {
            velocity.set(0, -1);
            targetRotation = 270;
        }

        if (Keyboard.isKeyDown(KEY_S) || Keyboard.isKeyDown(KEY_DOWN))
        {
            velocity.set(0, 1);
            targetRotation = 90;
        }

        if (Keyboard.isKeyDown(KEY_A) || Keyboard.isKeyDown(KEY_LEFT))
        {
            velocity.set(-1, 0);
            targetRotation = 180;
        }

        if (Keyboard.isKeyDown(KEY_D) || Keyboard.isKeyDown(KEY_RIGHT))
        {
            velocity.set(1, 0);
            targetRotation = 0;
        }

        velocity.normalize().scale(MathUtils.clamp(getLength(), 4, 12));
        rotateTo(targetRotation);

        if (position.x > SilenceEngine.display.getWidth() || position.x < 0 ||
            position.y > SilenceEngine.display.getHeight() || position.y < 0)
        {
            LDSnake.INSTANCE.setGameState(new LogoState());
        }
    }

    private void updateTail(float deltaTime)
    {
        if (prevCell.position.x == position.x && prevCell.position.y == position.y)
            return;

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

        final float speed = MathUtils.clamp(getLength(), 5, 12);
        final float clampedSpeed = Math.min(speed, Math.abs(clampedTarget - clampedCurrent));

        if (clampedCurrent < clampedTarget)
            rotation += clampedSpeed * sign;
        else
            rotation -= clampedSpeed * sign;
    }

    private void headTailCollision(Entity2D self, CollisionComponent2D otherComponent)
    {
        if (otherComponent.entity != nextCell && otherComponent.entity instanceof SnakeCell)
            LDSnake.INSTANCE.setGameState(new LogoState());

        else if (otherComponent.entity instanceof SnakeFood)
        {
            PlayState.DEAD.add(otherComponent.entity);

            SnakeCell last = this;

            while (last.nextCell != null)
                last = last.nextCell;

            SnakeCell newCell = new SnakeCell(last, PlayState.batch);
            PlayState.NEW.add(newCell);
        }
    }
}
