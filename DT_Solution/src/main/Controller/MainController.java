/**
 * Sample Skeleton for "simple.fxml" CreateJobController Class
 * Use copy/paste to copy paste this code into your favorite IDE
 **/

package main.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainController implements Initializable {
    @FXML
    Tab mCreateBill,mSettings;
    @FXML
    TabPane mTabPane;
    @FXML
    AnchorPane mAnchorPane;
    private Scene mScene;
    private Stage mMainStage;

    private QREditorController mCreateBillController;
    private SettingsController mSettingsController;




    public void setScene(Stage mainStage, Scene scene){
        mScene=scene;
        mMainStage=mainStage;


        try {
            // HBox of control buttons
            HBox hbox = new HBox();
            Button serverSyncButton=createTabButton("/refresh.png");
            hbox.getChildren().addAll(serverSyncButton);


            // Anchor the controls
            mAnchorPane.getChildren().addAll(hbox);
            AnchorPane.setTopAnchor(hbox, 3.0);
            AnchorPane.setRightAnchor(hbox, 20.0);
            AnchorPane.setTopAnchor(mTabPane, 1.0);
            AnchorPane.setRightAnchor(mTabPane, 1.0);
            AnchorPane.setLeftAnchor(mTabPane, 1.0);
            AnchorPane.setBottomAnchor(mTabPane, 1.0);


            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("QREditorController.fxml"));
            Node root = fxmlLoader.load();

            mCreateBill.setContent(root);
            mCreateBillController =  fxmlLoader.getController();
            mCreateBillController.setStage(mMainStage);








        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {


    }




    private Button createTabButton(String iconName) {
        Button button = new Button();
        ImageView imageView = new ImageView(new Image(getClass().getResource(iconName).toExternalForm(),
                16, 16, false, true));
        button.setGraphic(imageView);
        button.getStyleClass().add("tab-button");
        return button;
    }


    public Stage getMainStage(){return mMainStage;}
    public TabPane getTabPane(){return mTabPane;}
    public Scene getScene(){return mScene;}

    public QREditorController getCreateBillController(){return mCreateBillController;}

    public SettingsController getSettingsController(){return mSettingsController;}





}
