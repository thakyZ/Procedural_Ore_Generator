package map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MapData {
	
	public static final int FRONT = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int UP = 3;
	public static final int DOWN = 4;
	public static final int BACK = 5;
	
	public static final int MAP_SIDES = 1;//6;
	public static final int[][] adjacency = 
		{{},
			{},
			{},
			{},
			{},
			{}};
	
	public BufferedImage[] images;
	public BufferedImage[] surfaceHintImages;

	public static final int BACKGROUND_ORE_COLOUR = 255;
	public static final int BACKGROUND_ADD_COLOUR = 0xFF000000;

	int mapSize;
	
	public MapData() {
		images = new BufferedImage[MAP_SIDES];
		surfaceHintImages = new BufferedImage[MAP_SIDES];
	}
	
	public void clearOreData() {
		for(int i = 0; i < images.length; ++i) {
			BufferedImage img = images[i];
			if(img == null) {
				continue;
			}
			for(int j = 0; j < img.getWidth(); ++j) {
				for(int k = 0; k < img.getHeight(); ++k) {
					img.setRGB(j, k, img.getRGB(j, k) | BACKGROUND_ORE_COLOUR);
				}
			}
		}
		for(int i = 0; i < surfaceHintImages.length; ++i) {
			BufferedImage img = surfaceHintImages[i];
			if(img == null) {
				continue;
			}
			Graphics2D    graphics = img.createGraphics();
			graphics.setPaint ( new Color(BACKGROUND_ADD_COLOUR));
			graphics.fillRect (0, 0, img.getWidth(), img.getHeight());
		}
	}
	
	public int getMapSize() {
		return mapSize;
	}
	
	
	public void setMapSize(int mapSize) {
		this.mapSize = mapSize;
	}
	
	public void calculateMapSize() {
		if(this.images.length > 0) {
			if(this.images[0] != null) {
				//assume that all mapes are the same size and are square
				mapSize = this.images[0].getWidth();
			}
		}
	}
}
