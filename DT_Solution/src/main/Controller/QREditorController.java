/**
 * Sample Skeleton for "simple.fxml" CreateJobController Class
 * Use copy/paste to copy paste this code into your favorite IDE
 **/

package main.Controller;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.*;
import javafx.scene.paint.Color;


public class QREditorController implements Initializable {

    @FXML
    ImageView mImageView;
    @FXML
    Slider mSizeSlider;
    @FXML
    Button mButton;
    @FXML
    ColorPicker mBackgroundColorPicker, mForegroundColorPicker;

    Color mForegroundColor = Color.BLACK;
    Color mBackgroundColor = Color.WHITE;

    private double originalSize = 750;
    private MainController mMainController;
    final Image image = new Image("/qrcode.png", originalSize, originalSize, true, false);

    public void setScene(MainController mainController) {

        mMainController = mainController;
    }


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

        mImageView.setPreserveRatio(true);
        mImageView.setFitHeight(originalSize * 0.5);
        mImageView.setImage(image);
        mSizeSlider.setMin(0);
        mSizeSlider.setMax(100);
        mSizeSlider.setValue(50);
        mSizeSlider.setShowTickLabels(true);
        mSizeSlider.setShowTickMarks(true);

        //readImage();

        mSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                mImageView.setFitHeight(originalSize * (new_val.doubleValue() / 100));
            }
        });

        mForegroundColorPicker.setValue(mForegroundColor);
        mBackgroundColorPicker.setValue(mBackgroundColor);


        mForegroundColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mForegroundColor = mForegroundColorPicker.getValue();
                mBackgroundColor = mBackgroundColorPicker.getValue();
                mImageView.setImage(replaceByColor(mForegroundColor,mBackgroundColor));
            }
        });

        mBackgroundColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mForegroundColor = mForegroundColorPicker.getValue();
                mBackgroundColor = mBackgroundColorPicker.getValue();
                mImageView.setImage(replaceByColor(mForegroundColor,mBackgroundColor));
            }
        });


        /**  mButton.setOnAction(new EventHandler<ActionEvent>() {
        @Override public void handle(ActionEvent e) {
        mImageView.setImage(setBackgroundColor());
        }
        });**/

    }


    public void readImage() {

        // Obtain PixelReader
        PixelReader pixelReader = image.getPixelReader();
        System.out.println("Image Width: " + image.getWidth());
        System.out.println("Image Height: " + image.getHeight());
        System.out.println("Pixel Format: " + pixelReader.getPixelFormat());

        // Determine the color of each pixel in the image
        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                Color color = pixelReader.getColor(readX, readY);
                System.out.println("\nPixel color at coordinates ("
                        + readX + "," + readY + ") "
                        + color.toString());
                System.out.println("R = " + color.getRed());
                System.out.println("G = " + color.getGreen());
                System.out.println("B = " + color.getBlue());
                System.out.println("Opacity = " + color.getOpacity());
                System.out.println("Saturation = " + color.getSaturation());
            }
        }

    }

    public Image setForegroundColor() {
        return replaceByColor(Color.BLACK, Color.RED);
    }

    public Image setBackgroundColor() {
        return replaceByColor(Color.WHITE, Color.RED);

    }

    public Image replaceByColor(Color newFColor, Color newBColor) {
        System.err.println("Replacing Colors!!");
        PixelReader pixelReader = image.getPixelReader();
        // Create WritableImage
        WritableImage wImage = new WritableImage(
                (int) image.getWidth(),
                (int) image.getHeight());
        PixelWriter pixelWriter = wImage.getPixelWriter();

        // Determine the color of each pixel in a specified row
        for (int readY = 0; readY < image.getHeight(); readY++) {
            for (int readX = 0; readX < image.getWidth(); readX++) {
                Color cColor = pixelReader.getColor(readX, readY);
                // Now write a brighter color to the PixelWriter.
                //Foreground
                if (cColor.equals(Color.BLACK))
                    pixelWriter.setColor(readX, readY, newFColor);
                else
                    pixelWriter.setColor(readX, readY, newBColor);
            }
        }
        return wImage;

    }

}