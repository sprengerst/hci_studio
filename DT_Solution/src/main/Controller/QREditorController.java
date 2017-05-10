/**
 * Sample Skeleton for "simple.fxml" CreateJobController Class
 * Use copy/paste to copy paste this code into your favorite IDE
 **/

package main.Controller;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.paint.Color;


public class QREditorController implements Initializable {

    @FXML
    ImageView mImageView;
    @FXML
    Slider mSizeSlider,mEffectStrengthSlider;
    @FXML
    Button mButton;
    @FXML
    ColorPicker mBackgroundColorPicker, mForegroundColorPicker;
    @FXML
    ChoiceBox mChoiceBoxEffects;

    Color mForegroundColor = Color.BLACK;
    Color mBackgroundColor = Color.WHITE;
    Effect effects[];
    ObservableList<String> effectNames = FXCollections.observableArrayList();
    private double originalSize = 650;
    private MainController mMainController;
    final Image image = new Image("/qrcode.png", originalSize, originalSize, true, false);

    public void setScene(MainController mainController) {

        mMainController = mainController;
    }


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        initEffects();

        mChoiceBoxEffects.getSelectionModel().selectFirst();



        mImageView.setPreserveRatio(true);
        mImageView.setFitHeight(originalSize * 0.5);
        mImageView.setImage(image);


        mSizeSlider.setMin(0);
        mSizeSlider.setMax(100);
        mSizeSlider.setValue(50);
        mSizeSlider.setShowTickLabels(true);
        mSizeSlider.setShowTickMarks(true);

        mEffectStrengthSlider.setMin(0);
        mEffectStrengthSlider.setMax(100);
        mEffectStrengthSlider.setValue(50);
        mEffectStrengthSlider.setShowTickLabels(true);
        mEffectStrengthSlider.setShowTickMarks(true);
        //readImage();

        mSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                mImageView.setFitHeight(originalSize * ((new_val.doubleValue()+1) / 100));
            }
        });

        mEffectStrengthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
               int selectedIndex= mChoiceBoxEffects.getSelectionModel().getSelectedIndex();
                if(selectedIndex==1){
                    ((Bloom)effects[selectedIndex]).setThreshold(1-(new_val.doubleValue()/100));
                    mImageView.setEffect(effects[selectedIndex]);
                }
                else if(selectedIndex==2){
                    ((BoxBlur)effects[selectedIndex]).setIterations(new_val.intValue()/10);
                    ((BoxBlur)effects[selectedIndex]).setHeight(new_val.doubleValue()/3);
                    ((BoxBlur)effects[selectedIndex]).setWidth(new_val.doubleValue()/3);
                    mImageView.setEffect(effects[selectedIndex]);
                }else{
                    System.err.println(selectedIndex);
                }
            }
        });

        mChoiceBoxEffects.getSelectionModel().selectedIndexProperty()
                .addListener((ObservableValue<? extends Number> observable,
                              Number oldValue, Number newValue) -> {
                    mImageView.setEffect(effects[newValue.intValue()]);
                });

        mForegroundColorPicker.setValue(mForegroundColor);
        mBackgroundColorPicker.setValue(mBackgroundColor);


        mForegroundColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mForegroundColor = mForegroundColorPicker.getValue();
                mBackgroundColor = mBackgroundColorPicker.getValue();
                mImageView.setImage(replaceByColor(mForegroundColor, mBackgroundColor));
            }
        });

        mBackgroundColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mForegroundColor = mForegroundColorPicker.getValue();
                mBackgroundColor = mBackgroundColorPicker.getValue();
                mImageView.setImage(replaceByColor(mForegroundColor, mBackgroundColor));
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


    public void initEffects() {
        //Bloom effect
        Bloom bloom = new Bloom(0);

        //BoxBlur effect
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setWidth(50/3);
        boxBlur.setHeight(50/3);
        boxBlur.setIterations(5);

        //ColorAdjust effect
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setContrast(0.5);
        colorAdjust.setHue(-0.05);
        colorAdjust.setBrightness(0.5);
        colorAdjust.setSaturation(0.5);



        //DisplacementMap effect
        FloatMap floatMap = new FloatMap();
        floatMap.setWidth((int) originalSize);
        floatMap.setHeight((int) originalSize);

        for (int i = 0; i < originalSize; i++) {
            double v = (Math.sin(i / 20.0 * Math.PI) - 0.5) / 40.0;
            for (int j = 0; j < originalSize; j++) {
                floatMap.setSamples(i, j, 0.0f, (float) v);
            }
        }
        DisplacementMap displacementMap = new DisplacementMap();
        displacementMap.setMapData(floatMap);

        //DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(10.0);
        dropShadow.setOffsetX(20.0);
        dropShadow.setOffsetY(10.0);
        dropShadow.setColor(Color.GREY);

        //GaussianBlur effect
        GaussianBlur gaussianBlur = new GaussianBlur();

        //Glow effect
        Glow glow = new Glow(1.0);


        //Lighting effect
        Light.Distant light = new Light.Distant();
        light.setAzimuth(50.0);
        light.setElevation(30.0);
        light.setColor(Color.YELLOW);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(50.0);

        //MotionBlur effect
        MotionBlur motionBlur = new MotionBlur();
        motionBlur.setRadius(30);
        motionBlur.setAngle(-15.0);

        //Reflection effect
        Reflection reflection = new Reflection();
        reflection.setFraction(0.7);


        effects=new Effect[11];
        effects[0] = null;
        effects[1] = bloom;
        effects[2] = boxBlur;
        effects[3] = colorAdjust;
        effects[4] = displacementMap;
        effects[5] = dropShadow;
        effects[6] = gaussianBlur;
        effects[7] = glow;
        effects[8] = lighting;
        effects[9] = motionBlur;
        effects[10] = reflection;


        mChoiceBoxEffects.setItems(
                FXCollections.observableArrayList(
                        "null", "Bloom", "BoxBlur", "ColorAdjust",
                        "DisplacementMap", "DropShadow",
                        "GaussianBlur", "Glow",
                        "Lighting", "MotionBlur",
                        "Reflection"
                ));

    }

}