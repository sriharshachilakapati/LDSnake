package com.shc.ldsnake;

import com.shc.silenceengine.graphics.DynamicRenderer;
import com.shc.silenceengine.graphics.opengl.Primitive;
import com.shc.silenceengine.graphics.opengl.Texture;

/**
 * @author Sri Harsha Chilakapati
 */
public class RenderUtils
{
    public static void renderTexture(Texture texture, float x, float y, float width, float height)
    {
        DynamicRenderer dynamicRenderer = Resources.Renderers.DYNAMIC;
        Resources.Programs.DYNAMIC.use();

        texture.bind(0);
        dynamicRenderer.begin(Primitive.TRIANGLE_FAN);
        {
            dynamicRenderer.vertex(x, y);
            dynamicRenderer.texCoord(0, 0);

            dynamicRenderer.vertex(x + width, y);
            dynamicRenderer.texCoord(1, 0);

            dynamicRenderer.vertex(x + width, y + height);
            dynamicRenderer.texCoord(1, 1);

            dynamicRenderer.vertex(x, y + height);
            dynamicRenderer.texCoord(0, 1);
        }
        dynamicRenderer.end();
    }
}
