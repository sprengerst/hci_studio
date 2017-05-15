/**
 * Sample Skeleton for "simple.fxml" CreateJobController Class
 * Use copy/paste to copy paste this code into your favorite IDE
 **/

package main.Controller;



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.ResourceBundle;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class QREditorController implements Initializable {

    @FXML
    ImageView mImageView,mBackgroundImageView;
    @FXML
    Slider mSizeSlider,mEffectStrengthSlider,mOpacitySlider;
    @FXML
    Button mSubmitButton;
    @FXML
    ColorPicker mBackgroundColorPicker, mForegroundColorPicker;
    @FXML
    ChoiceBox mChoiceBoxEffects,mErrorChoiceBox;
    @FXML
    TextArea mEncodeTextArea;
    @FXML
    BorderPane mBorderPane;
    private Color mForegroundColor = Color.BLACK;
    private Color mBackgroundColor = Color.WHITE;
    private double mOpacityStrength=1.0;
    private double mEffectStrength=1.0;
    private ErrorCorrectionLevel mErrorCorrectionLevel=ErrorCorrectionLevel.L;

    private Effect effects[];
    ObservableList<String> effectNames = FXCollections.observableArrayList();
    private double originalSize = 650;
    private MainController mMainController;

    Image image = new Image("/qrcode.png", originalSize, originalSize, true, false);
    final Image backgroundImage=new Image("/background.png",originalSize,originalSize,true,false);
    public void setScene(MainController mainController) {
        mMainController = mainController;
    }


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        mEncodeTextArea.setText("Hallo Welt");


        Rectangle rect = createDraggableRectangle(200, 200, 400, 300);
        rect.setFill(Color.NAVY);

        mBorderPane.getChildren().add(rect);

        try {
            image = SwingFXUtils.toFXImage(createQRImage(mEncodeTextArea.getText(), (int) originalSize,mErrorCorrectionLevel), null);
        }catch(Exception ex){

        }


        initEffects();

        mChoiceBoxEffects.getSelectionModel().selectFirst();
        mErrorChoiceBox.getSelectionModel().selectFirst();

        mImageView.setPreserveRatio(true);
        mImageView.setFitHeight(originalSize * 0.5);
        mImageView.setImage(image);

        mBackgroundImageView.setPreserveRatio(true);
        mBackgroundImageView.setFitHeight(originalSize*0.5);
        mBackgroundImageView.setImage(backgroundImage);


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
        mOpacitySlider.setMin(0);
        mOpacitySlider.setMax(100);
        mOpacitySlider.setValue(100);
        mOpacitySlider.setShowTickLabels(true);
        mOpacitySlider.setShowTickMarks(true);

        mSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                mImageView.setFitHeight(originalSize * ((new_val.doubleValue()+1) / 100));
                mBackgroundImageView.setFitHeight(originalSize * ((new_val.doubleValue()+1) / 100));
            }
        });

        mOpacitySlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number old_val, Number new_val) {
                mOpacityStrength = (new_val.doubleValue()) / 100;
                mImageView.setImage(replaceByColor(mForegroundColor, mBackgroundColor,mOpacityStrength));
            }
        });

        mEffectStrengthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
               int selectedIndex = mChoiceBoxEffects.getSelectionModel().getSelectedIndex();
               mEffectStrength = new_val.doubleValue();
               setEffect(selectedIndex);

            }
        });
        mSubmitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    image = SwingFXUtils.toFXImage(createQRImage(mEncodeTextArea.getText(), (int) originalSize, mErrorCorrectionLevel), null);
                }catch(Exception ex){

                }
                mImageView.setImage(image);
            }
        });

        mErrorChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener((ObservableValue<? extends Number> observable,
                              Number oldValue, Number newValue) -> {
            switch(newValue.intValue()){
                case 0: mErrorCorrectionLevel=ErrorCorrectionLevel.L;break;
                case 1: mErrorCorrectionLevel=ErrorCorrectionLevel.M;break;
                case 2: mErrorCorrectionLevel=ErrorCorrectionLevel.Q;break;
                case 3: mErrorCorrectionLevel=ErrorCorrectionLevel.H;break;
            }

                });


        mChoiceBoxEffects.getSelectionModel().selectedIndexProperty()
                .addListener((ObservableValue<? extends Number> observable,
                              Number oldValue, Number newValue) -> {
                    if(newValue.intValue()==2 || newValue.intValue()==3 || newValue.intValue()==7)
                        mEffectStrengthSlider.setDisable(true);
                    else{
                        mEffectStrengthSlider.setDisable(false);
                    }
                    setEffect(newValue.intValue());
                });

        mForegroundColorPicker.setValue(mForegroundColor);
        mBackgroundColorPicker.setValue(mBackgroundColor);

        mForegroundColor = mForegroundColorPicker.getValue();
        mBackgroundColor = mBackgroundColorPicker.getValue();
        mImageView.setImage(replaceByColor(mForegroundColor, mBackgroundColor,mOpacityStrength));

        mForegroundColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mForegroundColor = mForegroundColorPicker.getValue();
                mBackgroundColor = mBackgroundColorPicker.getValue();
                mImageView.setImage(replaceByColor(mForegroundColor, mBackgroundColor,mOpacityStrength));
            }
        });

        mBackgroundColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                mForegroundColor = mForegroundColorPicker.getValue();
                mBackgroundColor = mBackgroundColorPicker.getValue();
                mImageView.setImage(replaceByColor(mForegroundColor, mBackgroundColor,mOpacityStrength));
            }
        });



    }

    public void setEffect(int selectedIndex){
         if(selectedIndex==1){
            ((BoxBlur)effects[selectedIndex]).setIterations((int)(mEffectStrength/10));
            ((BoxBlur)effects[selectedIndex]).setHeight(mEffectStrength/3);
            ((BoxBlur)effects[selectedIndex]).setWidth(mEffectStrength/3);
        }else if(selectedIndex==4){
            //Disable effect strength slider
            ((DropShadow)effects[selectedIndex]).setRadius(mEffectStrength);
            ((DropShadow)effects[selectedIndex]).setOffsetX(mEffectStrength);
            ((DropShadow)effects[selectedIndex]).setOffsetY(mEffectStrength);
            ((DropShadow)effects[selectedIndex]).setColor(Color.GREY);
        }else if(selectedIndex==5){
            ((GaussianBlur)effects[selectedIndex]).setRadius((63*(int) mEffectStrength)/100);
        }else if(selectedIndex==6){
            ((Glow) effects[selectedIndex]).setLevel((mEffectStrength/100));
        }else if(selectedIndex==8){
            ((MotionBlur) effects[selectedIndex]).setRadius(mEffectStrength);
        }else if(selectedIndex==9){
            ((Reflection) effects[selectedIndex]).setFraction(mEffectStrength/100);
        }

        mImageView.setEffect(effects[selectedIndex]);
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



    public Image replaceByColor(Color newFColor, Color newBColor,double opacity) {

        newFColor = new Color(newFColor.getRed(),newFColor.getGreen(),newFColor.getBlue(),opacity);
        newBColor = new Color(newBColor.getRed(),newBColor.getGreen(),newBColor.getBlue(),opacity);

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

    private static BufferedImage createQRImage(String qrCodeText, int size, ErrorCorrectionLevel errorCorrectionLevel) throws WriterException, IOException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable hintMap = new Hashtable();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, errorCorrectionLevel);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
                BarcodeFormat.QR_CODE, size, size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
                BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(java.awt.Color.white);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(java.awt.Color.black);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        return image;
    }


    public void initEffects() {
        mEffectStrength = 50;



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
        gaussianBlur.setRadius((63*50)/100);
        //Glow effect
        Glow glow = new Glow(1.0);


        //Lighting effect
        Light.Distant light = new Light.Distant();
        light.setAzimuth(-135.0);
        light.setColor(Color.YELLOW);
        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(150.0f);

        //MotionBlur effect
        MotionBlur motionBlur = new MotionBlur();
        motionBlur.setRadius(10);
        motionBlur.setAngle(-15.0);

        //Reflection effect
        Reflection reflection = new Reflection();
        reflection.setFraction(0.7);


        effects=new Effect[11];
        effects[0] = null;
        effects[1] = boxBlur;
        effects[2] = colorAdjust;
        effects[3] = displacementMap;
        effects[4] = dropShadow;
        effects[5] = gaussianBlur;
        effects[6] = glow;
        effects[7] = lighting;
        effects[8] = motionBlur;
        effects[9] = reflection;


        mChoiceBoxEffects.setItems(
                FXCollections.observableArrayList(
                        "null", "BoxBlur", "ColorAdjust",
                        "DisplacementMap", "DropShadow",
                        "GaussianBlur", "Glow",
                        "Lighting", "MotionBlur",
                        "Reflection"
                ));
        mErrorChoiceBox.setItems(FXCollections.observableArrayList(
                "Level L (7%)","Level M (15%)",
                "Level Q (25%)","Level H (30%)"
        ));

    }


    private Rectangle createDraggableRectangle(double x, double y, double width, double height) {
        final double handleRadius = 10 ;

        Rectangle rect = new Rectangle(x, y, width, height);

        // top left resize handle:
        Circle resizeHandleNW = new Circle(handleRadius, Color.GOLD);
        // bind to top left corner of Rectangle:
        resizeHandleNW.centerXProperty().bind(rect.xProperty());
        resizeHandleNW.centerYProperty().bind(rect.yProperty());

        // bottom right resize handle:
        Circle resizeHandleSE = new Circle(handleRadius, Color.GOLD);
        // bind to bottom right corner of Rectangle:
        resizeHandleSE.centerXProperty().bind(rect.xProperty().add(rect.widthProperty()));
        resizeHandleSE.centerYProperty().bind(rect.yProperty().add(rect.heightProperty()));

        // move handle:
        Circle moveHandle = new Circle(handleRadius, Color.GOLD);
        // bind to bottom center of Rectangle:
        moveHandle.centerXProperty().bind(rect.xProperty().add(rect.widthProperty().divide(2)));
        moveHandle.centerYProperty().bind(rect.yProperty().add(rect.heightProperty()));

        // force circles to live in same parent as rectangle:
        rect.parentProperty().addListener((obs, oldParent, newParent) -> {
            for (Circle c : Arrays.asList(resizeHandleNW, resizeHandleSE, moveHandle)) {
                Pane currentParent = (Pane)c.getParent();
                if (currentParent != null) {
                    currentParent.getChildren().remove(c);
                }
                ((Pane)newParent).getChildren().add(c);
            }
        });

        Wrapper<Point2D> mouseLocation = new Wrapper<>();

        setUpDragging(resizeHandleNW, mouseLocation) ;
        setUpDragging(resizeHandleSE, mouseLocation) ;
        setUpDragging(moveHandle, mouseLocation) ;

        resizeHandleNW.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = rect.getX() + deltaX ;
                if (newX >= handleRadius
                        && newX <= rect.getX() + rect.getWidth() - handleRadius) {
                    rect.setX(newX);
                    rect.setWidth(rect.getWidth() - deltaX);
                }
                double newY = rect.getY() + deltaY ;
                if (newY >= handleRadius
                        && newY <= rect.getY() + rect.getHeight() - handleRadius) {
                    rect.setY(newY);
                    rect.setHeight(rect.getHeight() - deltaY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });

        resizeHandleSE.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newMaxX = rect.getX() + rect.getWidth() + deltaX ;
                if (newMaxX >= rect.getX()
                        && newMaxX <= rect.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    rect.setWidth(rect.getWidth() + deltaX);
                }
                double newMaxY = rect.getY() + rect.getHeight() + deltaY ;
                if (newMaxY >= rect.getY()
                        && newMaxY <= rect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    rect.setHeight(rect.getHeight() + deltaY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }
        });

        moveHandle.setOnMouseDragged(event -> {
            if (mouseLocation.value != null) {
                double deltaX = event.getSceneX() - mouseLocation.value.getX();
                double deltaY = event.getSceneY() - mouseLocation.value.getY();
                double newX = rect.getX() + deltaX ;
                double newMaxX = newX + rect.getWidth();
                if (newX >= handleRadius
                        && newMaxX <= rect.getParent().getBoundsInLocal().getWidth() - handleRadius) {
                    rect.setX(newX);
                }
                double newY = rect.getY() + deltaY ;
                double newMaxY = newY + rect.getHeight();
                if (newY >= handleRadius
                        && newMaxY <= rect.getParent().getBoundsInLocal().getHeight() - handleRadius) {
                    rect.setY(newY);
                }
                mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
            }

        });

        return rect ;
    }

    private void setUpDragging(Circle circle, Wrapper<Point2D> mouseLocation) {

        circle.setOnDragDetected(event -> {
            circle.getParent().setCursor(Cursor.CLOSED_HAND);
            mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
        });

        circle.setOnMouseReleased(event -> {
            circle.getParent().setCursor(Cursor.DEFAULT);
            mouseLocation.value = null ;
        });
    }

    static class Wrapper<T> { T value ; }
}