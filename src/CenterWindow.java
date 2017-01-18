/**
 * Created by muniz on 10/10/16.
 */
import javax.swing.*;
import java.awt.*;

/**
 * This class is used to center the window
 * based on the specified methods.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

public final class CenterWindow {

    /**
     * This method is used to center a window
     * to the middle of the screen.
     * @param window the window to center
     */

    public static void centerOnScreen(final Component window) {
        GraphicsConfiguration config = window.getGraphicsConfiguration();
        GraphicsDevice currentScreen = config.getDevice();
        GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] allScreens = environment.getScreenDevices();
        GraphicsDevice activeScreen = null;

        for (GraphicsDevice screenI : allScreens) {
            if (screenI.equals(currentScreen)) {
                activeScreen = screenI;
                break;
            }
        }
        final int width = window.getWidth();
        final int height = window.getHeight();
        final int screenWidth = activeScreen.getDisplayMode().getWidth();
        final int screenHeight = activeScreen.getDisplayMode().getHeight();
        int x = (screenWidth / 2) - (width / 2);
        int y = (screenHeight / 2) - (height / 2);
        window.setLocation(x, y);
    }

    /**
     * This method is used to center a window
     * on another window.
     * @param guideWindow the window to be used as a guide
     * @param windowToBeCentered the window that will be centered on the guide window
     */

    public static void centerOnWindow(final JFrame guideWindow, final JFrame windowToBeCentered) {
        int x = guideWindow.getX() + (guideWindow.getWidth()/2 - windowToBeCentered.getWidth()/2);
        int y = guideWindow.getY();

        windowToBeCentered.setLocation(x, y);
    }
}
