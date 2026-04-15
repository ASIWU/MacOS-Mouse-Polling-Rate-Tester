package cz.pscheidl.mouse.settings;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.ImageIcon;

public class Settings {
    private static final Color bgColor = new Color(30, 101, 134);
    private static Font mouseFont;
    private static final ImageIcon appIcon = loadIcon();

    public static ImageIcon getAppicon() {
        return appIcon;
    }

    public static Color getBgcolor() {
        return bgColor;
    }

    public static Font getMouseFont() {
        if (mouseFont == null) {
            try (InputStream stream = Settings.class.getResourceAsStream("/cz/pscheidl/mouse/files/mouseFont.ttf")) {
                if (stream != null) {
                    mouseFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(14.0f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mouseFont;
    }

    private static ImageIcon loadIcon() {
        java.net.URL resource = Settings.class.getResource("/cz/pscheidl/mouse/files/mouseIcon.png");
        return resource == null ? new ImageIcon() : new ImageIcon(resource);
    }
}
