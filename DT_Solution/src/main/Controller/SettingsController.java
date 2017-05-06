package main.Controller; /**
 * Sample Skeleton for "simple.fxml" CreateJobController Class
 * Use copy/paste to copy paste this code into your favorite IDE
 **/


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;


public class SettingsController implements Initializable {


    @FXML
    private TextField mNextJobPositionTextField, mNextBillPositionTextField;



    MainController mMainController;

    public void setScene(MainController mainController) {
        mMainController=mainController;
    }


    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {



    }

    public void setNextBillPosition(String nextBillPosition){
        mNextBillPositionTextField.setText(nextBillPosition);
    }
    public void setNextJobPosition(String nextJobPosition){
        mNextJobPositionTextField.setText(nextJobPosition);
    }




}