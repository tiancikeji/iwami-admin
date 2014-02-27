package com.iwami.iwami.app.util;

import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
//import javax.imageio.ImageIO;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.Binarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.BarcodeFormat;

public class TwoDCodeUtil {

	public static byte[] get2DCode(String userid) {
		BufferedImage image;
		try {
			image = TwoDCodeUtil.encode(userid);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			
			return baos.toByteArray();
		} catch (WriterException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected static BufferedImage encode(String name) throws WriterException, IOException {
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = multiFormatWriter.encode(name, BarcodeFormat.QR_CODE, 400, 400, hints);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	protected static String decode(BufferedImage image) throws WriterException, IOException, NotFoundException{
		LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer  binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        return new MultiFormatReader().decode(binaryBitmap,hints).getText();
	}
}
