package ua.eyecare.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import ua.eyecare.Launcher;
import ua.eyecare.model.EyeCare;
import ua.eyecare.model.Localizable;
import ua.eyecare.tools.StringLoader;
import ua.eyecare.tools.TimeFormat;

import java.io.IOException;


/**
 * Created by bliareg on 29.06.15.
 */
public class SmallEyeCareController implements EyeCare, Localizable {

    @FXML private Label message;
    @FXML private Label timer;
    @FXML private Button settingsButton;
    @FXML private Button exitButton;
    private MainApp mainApp;
    private AnchorPane pane;
    private StringLoader loader;

    private Tooltip settingsToolTip;
    private Tooltip exitToolTip;

    private int numMessage;

    @FXML
    public void handleShowSettings(){
        try {

            mainApp.showSettingsWindow();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleExit(){
        Launcher.shutDownApp();
        System.exit(0);
    }

    @FXML
    private void initialize(){
        settingsToolTip = new Tooltip();
        exitToolTip = new Tooltip();

        settingsButton.setTooltip(settingsToolTip);
        exitButton.setTooltip(exitToolTip);

        firstInitStringResources fisr = new firstInitStringResources(); fisr.start();
    }

    @Override
    public void setTimerTime(int time) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                timer.setText(TimeFormat.convertTimeToView(time));
            }
        });
    }

    @Override
    public AnchorPane getPane() {
        return pane;
    }

    @Override
    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }

    @Override
    public void setStringLoader(StringLoader l) {
        this.loader = l;
    }

    @Override
    public void initAllStringResources() {
        settingsToolTip.setText(loader.getString("SW_settingsToolTip"));
        exitToolTip.setText(loader.getString("SW_exitToolTip"));
    }

    @Override
    public void readyToLaunch() {
        if(!mainApp.isFirstLaunch()){
//            TODO Number format exception for input string "7", throws line below
            int messagesCount = 7;     /*Integer.parseInt(loader.getString("message_count"));*/
            int numMessage = (int) (Math.random() * messagesCount) + 1;
            this.numMessage = numMessage;
        }else {
            this.numMessage = -1;
            mainApp.setFirstLaunch(false);
        }
        writeMessage();
        timer.setText("");
    }

        private void writeMessage(){
            if(numMessage==0)numMessage = (int) (Math.random()*Integer.parseInt(loader.getString("message_count")))+1;
            if(numMessage==-1)
                message.setText(loader.getString("SW_Intro"));
            else
                message.setText(loader.getString("SW_message"+numMessage));
        }
    @Override
    public void languageWasChanged() {
        initAllStringResources();
    writeMessage();
    }

    public void setMainApp(MainApp m) {
        this.mainApp = m;
    }

    public MainApp getMainApp() {
        return mainApp;
    }



    private class firstInitStringResources extends Thread{
        @Override
        public void run() {
         while(loader==null){
             try {Thread.sleep(350);} catch (InterruptedException e) {e.printStackTrace();}
         }
                initAllStringResources();
        }
    }
}
