package main;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Iterator;


public class KeyEventHandler implements EventHandler {


    private VBox mVBox;
    private Iterator<Node> mVBoxIterator;
    private ArrayList<Control> mControlFields;
    private Scene mScene;

    public KeyEventHandler(Scene scene,VBox vBox){
        this.mVBox = vBox;
        this.mScene=scene;
        mControlFields=new ArrayList<Control>();
        Iterator<Node> mVBoxIterator = mVBox.getChildren().iterator();

        while(mVBoxIterator.hasNext()){
            Node cNode=mVBoxIterator.next();
            if(cNode instanceof HBox)
                addHBoxControlElements((HBox) cNode);
            else if(cNode instanceof Control && !(cNode instanceof Label) && !(cNode instanceof DateTimePicker))
                mControlFields.add((Control)cNode);
        }
        for (int i=0;i<mControlFields.size();i++){
            System.out.println(mControlFields.get(i));
        }

    }


    public void addHBoxControlElements(HBox hBox){
        Iterator<Node> hBoxIterator = hBox.getChildren().iterator();
        while(hBoxIterator.hasNext()){
            Node cNode=hBoxIterator.next();
            if(cNode instanceof HBox)
                addHBoxControlElements((HBox) cNode);
            else if(cNode instanceof Control && !(cNode instanceof Label) && !(cNode instanceof DateTimePicker)) {

                mControlFields.add((Control) cNode);
            }

        }

    }

    @Override
    public void handle(Event event) {
        if (event.getEventType()== KeyEvent.KEY_RELEASED){
            int index=mControlFields.indexOf(mScene.getFocusOwner());

            if(index<0 || mControlFields.get(index) instanceof ListView)
                return;
            KeyEvent keyEvent=(KeyEvent) event;
            if(keyEvent.getCode()==KeyCode.ENTER)
                mControlFields.get(index + 1).requestFocus();
            /**else if(keyEvent.getCode()==KeyCode.UP) {
                if(index-1>=0)
                    mControlFields.get(index - 1).requestFocus();
                else
                    mControlFields.get(mControlFields.size()-1).requestFocus();
            }
            else if(keyEvent.getCode()==KeyCode.DOWN)
                if(index+1<mControlFields.size())
                    mControlFields.get(index + 1).requestFocus();
                else
                    mControlFields.get(0).requestFocus();**/

        }

    }
}
