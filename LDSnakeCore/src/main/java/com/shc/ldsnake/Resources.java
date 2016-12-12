package com.shc.ldsnake;

import com.shc.silenceengine.graphics.SpriteRenderer;
import com.shc.silenceengine.graphics.fonts.BitmapFont;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.graphics.opengl.Texture;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Resources
{
    public static final class Textures
    {
        public static Texture SNAKE_CELL;
    }

    public static final class Fonts
    {
        public static BitmapFont NORMAL;
    }

    public static final class Renderers
    {
        public static SpriteRenderer     SPRITE;
        public static BitmapFontRenderer BITMAP_FONT;
    }
}
