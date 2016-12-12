package com.shc.ldsnake;

import com.shc.silenceengine.collision.CollisionTag;
import com.shc.silenceengine.graphics.Animation;
import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.SpriteRenderer;
import com.shc.silenceengine.graphics.fonts.BitmapFont;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.graphics.programs.DynamicProgram;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Resources
{
    public static final class Textures
    {
        public static Texture SNAKE_FOOD;
        public static Texture SNAKE_CELL;
        public static Texture SNAKE_CONN;
        public static Texture BACKGROUND;
    }

    public static final class Animations
    {
        public static Animation SNAKE_HEAD;
    }

    public static final class Programs
    {
        public static DynamicProgram DYNAMIC;
    }

    public static final class Fonts
    {
        public static BitmapFont NORMAL;
    }

    public static final class CollisionTags
    {
        public static final CollisionTag SNAKE_CELL = new CollisionTag();
        public static final CollisionTag SNAKE_HEAD = new CollisionTag();
        public static final CollisionTag SNAKE_FOOD = new CollisionTag();
    }

    public static final class Renderers
    {
        public static SpriteRenderer     SPRITE;
        public static DynamicRenderer    DYNAMIC;
        public static BitmapFontRenderer BITMAP_FONT;
    }
}
