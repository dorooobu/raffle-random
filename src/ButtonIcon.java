import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class ButtonIcon extends JButton {

	private BufferedImage icon, iconHover;
	private boolean isHover = false;
	
	public ButtonIcon(String url, String tooltip) {
		icon = ImageLoader.getImage(url);
		iconHover = ImageLoader.getImage(url + "_hover");
		setSize(30, 25);
		setBorderPainted(false);
		setOpaque(false);
		setToolTipText(tooltip);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent mE) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				isHover = true;
			}
			
			@Override
			public void mouseExited(MouseEvent mE) {
				isHover = false;
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(isHover == true ? iconHover: icon, 0, 0, null);
		g.dispose();
	}
	
}