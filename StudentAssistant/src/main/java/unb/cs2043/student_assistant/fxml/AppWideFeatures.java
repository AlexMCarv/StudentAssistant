package unb.cs2043.student_assistant.fxml;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageRange;
import javafx.print.PrinterJob;
import javafx.scene.image.Image;
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
        Image image = getSnapshot(node);
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
	
	public static Image getSnapshot(Region layout) {
		WritableImage writableImage = new WritableImage((int)layout.getWidth() + 20,
                (int)layout.getHeight() + 20);
        layout.snapshot(null, writableImage);
        BufferedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
        Image image = SwingFXUtils.toFXImage(renderedImage, writableImage);
        return image;
	}
}
