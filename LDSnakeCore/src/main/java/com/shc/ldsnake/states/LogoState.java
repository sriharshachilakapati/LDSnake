package com.shc.ldsnake.states;

import com.shc.ldsnake.LDSnake;
import com.shc.ldsnake.RenderUtils;
import com.shc.ldsnake.Resources;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.IGraphicsDevice;
import com.shc.silenceengine.graphics.cameras.OrthoCam;
import com.shc.silenceengine.graphics.fonts.BitmapFontRenderer;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.input.Keyboard;

/**
 * @author Sri Harsha Chilakapati
 */
public class LogoState extends GameState
{
    private OrthoCam camera;

    @Override
    public void onEnter()
    {
        camera = new OrthoCam(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isKeyTapped(Keyboard.KEY_SPACE))
            LDSnake.INSTANCE.setGameState(new PlayState());
    }

    @Override
    public void render(float delta)
    {
        camera.apply();

        RenderUtils.renderTexture(Resources.Textures.BACKGROUND, 0, 0,
                SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());

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

            message = "Press SPACE to find out how long can you survive";
            messageX = SilenceEngine.display.getWidth() / 2 - Resources.Fonts.NORMAL.getWidth(message) / 2;
            messageY = SilenceEngine.display.getHeight() - Resources.Fonts.NORMAL.getHeight() - 50;

            fontRenderer.render(Resources.Fonts.NORMAL, message, messageX, messageY);
        }
        fontRenderer.end();

        Texture logo = Resources.Textures.LOGO;

        float centerX = SilenceEngine.display.getWidth() / 2 - logo.getWidth() / 2;
        float centerY = SilenceEngine.display.getHeight() / 2 - logo.getHeight() / 2;

        RenderUtils.renderTexture(logo, centerX, centerY,
                logo.getWidth(), logo.getHeight());
    }

    @Override
    public void resized()
    {
        camera.initProjection(SilenceEngine.display.getWidth(), SilenceEngine.display.getHeight());
    }
}
