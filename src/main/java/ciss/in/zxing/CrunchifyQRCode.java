package ciss.in.zxing;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
 
/**
 * @author Crunchify.com
 */
 
public class CrunchifyQRCode {
 
    // Tutorial: https://github.com/Slackify/zxing
 
    @SuppressWarnings("unchecked")
	public void createQRCode() {
    	String qrCodeData = "http://www.ciss.in/";
    	Path filePath = FileSystems.getDefault().getPath("E:/", "cte.png");
    	String charset = "UTF-8"; // or "ISO-8859-1"
    	@SuppressWarnings("rawtypes")
    	Map hintMap = new HashMap();
    	hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);    	
    	int qrCodeheight = 200;
    	int qrCodewidth = 200;
		BitMatrix matrix;
		try {
			matrix = new MultiFormatWriter().encode(
					new String(qrCodeData.getBytes(charset), charset),
					BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
			MatrixToImageWriter.writeToPath(matrix, "png", filePath);
		} catch (WriterException | IOException e1) {
			e1.printStackTrace();
		}

        System.out.println("\n\nYou have successfully created QR Code.");
    }

    public String readQRCode(String filePath, String charset, Map<DecodeHintType, ?> hintMap) {
		BinaryBitmap binaryBitmap;
		Result qrCodeResult = null;
		try {
			binaryBitmap = new BinaryBitmap(new HybridBinarizer(
					new BufferedImageLuminanceSource(
							ImageIO.read(new FileInputStream(filePath)))));
			qrCodeResult = new MultiFormatReader().decode(binaryBitmap, hintMap);
		} catch (IOException | NotFoundException e) {
			e.printStackTrace();
		}
		return qrCodeResult.getText();
	}    	
}