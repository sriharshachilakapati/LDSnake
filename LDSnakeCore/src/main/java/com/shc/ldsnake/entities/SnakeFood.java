package com.shc.ldsnake.entities;

import com.shc.ldsnake.Resources;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Sprite;
import com.shc.silenceengine.graphics.SpriteBatch;
import com.shc.silenceengine.math.geom2d.Polygon;
import com.shc.silenceengine.math.geom2d.Rectangle;
import com.shc.silenceengine.scene.components.CollisionComponent2D;
import com.shc.silenceengine.scene.components.SpriteComponent;
import com.shc.silenceengine.scene.entity.Entity2D;
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class SnakeFood extends Entity2D
{
    public SnakeFood(SpriteBatch batch)
    {
        position.set(MathUtils.randomRange(0, SilenceEngine.display.getWidth()),
                MathUtils.randomRange(0, SilenceEngine.display.getHeight()));

        scale.set(0.25f);

        Polygon polygon = new Rectangle(64, 128).createPolygon();

        addComponent(new CollisionComponent2D(Resources.CollisionTags.SNAKE_FOOD, polygon));
        addComponent(new SpriteComponent(new Sprite(Resources.Textures.SNAKE_FOOD), batch));
    }
}
