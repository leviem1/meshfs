/**
 * Created by muniz on 10/10/16.
 */
import java.awt.*;

public class CenterWindow {
    public static void centerOnScreen(final Component c) {
        final int width = c.getWidth();
        final int height = c.getHeight();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width / 2) - (width / 2);
        int y = (screenSize.height / 2) - (height / 2);
        c.setLocation(x, y);
    }
}
