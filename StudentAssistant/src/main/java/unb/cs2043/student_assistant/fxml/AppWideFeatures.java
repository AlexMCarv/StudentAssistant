package unb.cs2043.student_assistant.fxml;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageRange;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

public class AppWideFeatures {
	
	public static void captureAndSave(Region layout){
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png files (*.png)", "*.png"));
	    File file = fileChooser.showSaveDialog(null);

	    if(file != null){
	        try {
	            WritableImage writableImage = new WritableImage((int)layout.getWidth() + 20,
	                    (int)layout.getHeight() + 20);
	            layout.snapshot(null, writableImage);
	            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
	            ImageIO.write(renderedImage, "png", file);
	        } catch (IOException ex) { ex.printStackTrace(); }
	    }
	}
	
	public static void print(Region node) {
        PrinterJob job = PrinterJob.createPrinterJob();
        JobSettings jobSettings = job.getJobSettings();
        PageLayout pageLayout = jobSettings.getPageLayout();
        javafx.scene.image.Image image = getSnapshot(node);
        jobSettings.setPageRanges(new PageRange(1, 1));
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(pageLayout.getPrintableHeight());
        imageView.setFitWidth(pageLayout.getPrintableWidth());
                
        if (job != null) {
            boolean success = job.showPrintDialog(node.getScene().getWindow());
            if (success) {
    	        boolean printed = job.printPage(imageView);
    	        if (printed)
    	        {
    	            job.endJob();
    	        }
            }
        }
    }
	
	public static javafx.scene.image.Image getSnapshot(Region layout) {
		WritableImage writableImage = new WritableImage((int)layout.getWidth() + 20,
                (int)layout.getHeight() + 20);
        layout.snapshot(null, writableImage);
        BufferedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        javafx.scene.image.Image image = SwingFXUtils.toFXImage(renderedImage, null);
        return image;
	}
	
	public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
		java.awt.Image tmp = img.getScaledInstance(newW, newH, java.awt.Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();

	    return dimg;
	}  
}
