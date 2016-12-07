/**
 * Created by muniz on 10/10/16.
 */
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
        final int width = window.getWidth();
        final int height = window.getHeight();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (width / 2);
        int y = (screenSize.height / 2) - (height / 2);
        window.setLocation(x, y);
    }

    /**
     * This method is used to center a window
     * on another window.
     * @param guideWindow the window to be used as a guide
     * @param windowToBeCentered the window that will be centered on the guide window
     */

    public static void centerOnWindow(final Component guideWindow, final Component windowToBeCentered) {
        int x = guideWindow.getX() + (guideWindow.getWidth()/2 - windowToBeCentered.getWidth()/2);
        int y = guideWindow.getY();

        windowToBeCentered.setLocation(x, y);
    }
}
