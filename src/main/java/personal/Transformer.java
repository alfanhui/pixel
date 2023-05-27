package personal;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


import com.github.sarxos.webcam.WebcamImageTransformer;
import com.jhlabs.image.ThresholdFilter;


public class Transformer implements WebcamImageTransformer {

    // threshold is a value in range of [1, 255]
    final ThresholdFilter binarizer = new ThresholdFilter(128);

	@Override
	public BufferedImage transform(BufferedImage image) {

		BufferedImage modified = new BufferedImage(128, 128, BufferedImage.TYPE_BYTE_GRAY);

        final BufferedImage binary = binarizer.filter(image, null);
        
		Graphics2D g2 = modified.createGraphics();
		g2.drawImage(binary, null, 0, 0);
		g2.dispose();

		modified.flush();

		return modified;
	}
}
