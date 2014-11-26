package szoftverfolyamat.osz.game.controller.sprites;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;

public interface BoundingShape {
	 Dimension getPrimaryBoundingBox();
	 
	 Rectangle2D[] getRefinementBoundingBoxes();
}
