package ua.eyecare.controller;/**
 * Created by bliareg on 29.06.15.
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import ua.eyecare.Launcher;
import ua.eyecare.model.EyeCare;
import ua.eyecare.model.Localizable;
import ua.eyecare.tools.AppProperties;
import ua.eyecare.tools.EyeCareTimer;
import ua.eyecare.tools.StringLoader;
import ua.eyecare.tools.TimeFormat;

import java.io.IOException;
import java.util.Set;
import java.util.TimerTask;

public class MainApp extends Application {


    private Stage primaryStage;
    private EyeCareTimer timer;
    private AppProperties props;
    private BorderPane mainPane;
    private boolean show = false; private EyeCare smallEyeCare;
    private EyeCare largeEyeCare;
    public static MainApp instance;
    private Set<Localizable> lobjs;
    private StringLoader loader;
    private boolean firstLaunch;


    private static double SMALL_WINDOW_WIDTH = 225;
    private static double SMALL_WINDOW_HEIGHT = 105;
    private static double LARGE_WINDOW_WIDTH = 400;
    private static double LARGE_WINDOW_HEIGHT = 200;

    public static MainApp getInstance(){
        return instance;
    }

    public static void createMainApp(String[] args){
        launch(args);
    }


    public void startApp(){

        initialize();

        if(new Boolean(props.getProperty(AppProperties.PROPERTIES.FIRST_LAUNCH)).booleanValue()) {
            firstLaunch = true;
            show = true;
            props.setProperty(AppProperties.PROPERTIES.FIRST_LAUNCH,"false");
            timer.setTask(new EyeCareTimerTask());

            try {showSettingsWindow();} catch (IOException e) {e.printStackTrace();}
            try {timer.start();} catch (Exception ex) {ex.printStackTrace();}
            showEyeCare();
        }else {
            timer.setTask(new EyeCareTimerTask());
            try {timer.start();} catch (Exception ex) {ex.printStackTrace();}
        }
    }


    @Override
    public void start(Stage primaryStage) {



        Stage showStage = new Stage();
        showStage.initModality(Modality.APPLICATION_MODAL);
        showStage.initStyle(StageStyle.UTILITY);

        primaryStage.setIconified(true);
        showStage.setIconified(true);

        Image img = new Image(getClass().getResourceAsStream("/images/EyeCare.png"));
        primaryStage.getIcons().add(img);
        showStage.getIcons().add(img);

        this.primaryStage = showStage;

        primaryStage.setAlwaysOnTop(true);

        instance = this;
        Platform.setImplicitExit(false);
        Launcher.launchContext();


//        showStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                showStage.close();
//            }
//        });
    }



        private void initialize(){
            initRootLayout();
            primaryStage.setOnHiding(event -> {
                props.setProperty(AppProperties.PROPERTIES.POSITION_X, primaryStage.getX());
                props.setProperty(AppProperties.PROPERTIES.POSITION_Y, primaryStage.getY());
            });
        }

        private void onDestroy(){
            props.setProperty(AppProperties.PROPERTIES.POSITION_X, primaryStage.getX());
            props.setProperty(AppProperties.PROPERTIES.POSITION_Y, primaryStage.getY());
        }


    private EyeCare loadSmallEyeCare() throws IOException   {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/ua/eyecare/view/SmallEyeCare.fxml"));
        AnchorPane anchorPane = loader.load();

        SmallEyeCareController c = loader.getController();

        c.setMainApp(this);

        EyeCare eyeCare = (EyeCare) c;
        eyeCare.setPane(anchorPane);

        this.smallEyeCare = eyeCare;

        lobjs.add((Localizable) c);
        return eyeCare;
    }

        private void showEyeCare(){

                    try {

                        setUpWindowPrefs();


                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                primaryStage.show();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

        private void hideEyeCare(){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    primaryStage.hide();
                }
            });

        }

        public void showSettingsWindow() throws IOException {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("/ua/eyecare/view/Settings.fxml"));
            AnchorPane anchorPane = loader.load();
            SettingsController c = loader.getController();

            c.setLoader(this.loader);
            c.setProperties(props);
            c.setLocalizables(lobjs);
            c.setT((EyeCareTimerTask) timer.getTask());
            Stage stage = new Stage();
            stage.setTitle(this.loader.getString("ST_title"));
            stage.initStyle(StageStyle.UTILITY);
//            stage.initOwner(primaryStage);
            stage.setScene(new Scene(anchorPane));
            stage.show();
        }

    private void setUpWindowPrefs() throws IOException {

        if(firstLaunch) primaryStage.setAlwaysOnTop(false);else if(!primaryStage.isAlwaysOnTop())primaryStage.setAlwaysOnTop(true);
        String type = props.getProperty(AppProperties.PROPERTIES.TYPE_OF_WINDOW);
        if(type.equals("small")){
            smallEyeCare.readyToLaunch();
            mainPane.setCenter(smallEyeCare.getPane());
            primaryStage.setX(props.getPropertyDouble(AppProperties.PROPERTIES.POSITION_X));
            primaryStage.setY(props.getPropertyDouble(AppProperties.PROPERTIES.POSITION_Y));
            primaryStage.setTitle("EyeCare");

        if(type.equals("large")){
            largeEyeCare.readyToLaunch();
            mainPane.setCenter(largeEyeCare.getPane());
            primaryStage.setX(props.getPropertyDouble(AppProperties.PROPERTIES.POSITION_X));
            primaryStage.setY(props.getPropertyDouble(AppProperties.PROPERTIES.POSITION_Y));
        }
    }}



    private void initRootLayout(){

        BorderPane mainPane = new BorderPane();
        primaryStage.setScene(new Scene(mainPane));
        this.mainPane = mainPane;
    }

    private void showLargeEyeCare(){
//        don`t forget add to localizable objects
    }

    public class EyeCareTimerTask extends TimerTask {


        private int time;
        private int showingTime;
        private int hidingTime;
        private boolean wait = false;
        private int waitCount;

        public EyeCareTimerTask() {
            this.hidingTime = props.getPropertyInteger(AppProperties.PROPERTIES.HIDING_TIME);
            this.showingTime = props.getPropertyInteger(AppProperties.PROPERTIES.SHOWING_TIME);

            if(show)
                this.time = showingTime;
            else
                this.time = hidingTime;

        }

        @Override
        public void run(){
            if(!wait){
            if(show) {
                showingWindow();
                System.out.println("----------TIMES_LEFT_TO_HIDE----------" + TimeFormat.convertTimeToView(time) + "----------TIMES_LEFT_TO_HIDE----------");
            }else{
                hidingWindow();
                System.out.println("----------TIMES_LEFT_TO_SHOW----------" + TimeFormat.convertTimeToView(time) + "----------TIMES_LEFT_TO_SHOW----------");
            }
            }else if(waitCount!=3)waitCount++;else{ waitCount = 0; wait = false; try {props.save();} catch (IOException e) {e.printStackTrace();}}
        }
        public void waitNow(){
            wait = true;
            waitCount = 0;
        }

        public void showingWindow(){
        time--;
            if(time<=0){
                show=false;
                hidingTime = props.getPropertyInteger(AppProperties.PROPERTIES.HIDING_TIME);
                time = hidingTime;
                hideEyeCare();
                try {props.save();} catch (IOException e) {e.printStackTrace();}

            }else{
                smallEyeCare.setTimerTime(time);
            }
        }


        public void hidingWindow(){
            time--;
            if(time<=0){
                show = true;
                showingTime = props.getPropertyInteger(AppProperties.PROPERTIES.SHOWING_TIME);
                time = showingTime;
                showEyeCare();
            }
        }


        public void unexpectedlyChangeTime(){
//            if(show){
//                int coutedTime = showingTime-time;
//                showingTime = props.getPropertyInteger(AppProperties.PROPERTIES.SHOWING_TIME);
//                time = showingTime - time;
//            time = props.getPropertyInteger(AppProperties.PROPERTIES.SHOWING_TIME);
//                smallEyeCare.setTimerTime(time);

//            } else{
//                int coutedTime = hidingTime-time;
//                hidingTime = props.getPropertyInteger(AppProperties.PROPERTIES.HIDING_TIME);
//                time = hidingTime - time;
//            time = props.getPropertyInteger(AppProperties.PROPERTIES.HIDING_TIME);
//            }

                time = props.getPropertyInteger(AppProperties.PROPERTIES.SHOWING_TIME);
            if(!show) showEyeCare();
            show = true;
            smallEyeCare.setTimerTime(time);

        }


    }






    public AppProperties getProps() {
        return props;
    }

    public void setProps(AppProperties props) {
        this.props = props;
    }

    public EyeCareTimer getTimer() {
        return timer;
    }

    public void setTimer(EyeCareTimer timer) {
        this.timer = timer;
    }



    public void setLobjs(Set<Localizable> lobjs) {
        this.lobjs = lobjs;
    }

    public void setLoader(StringLoader loader) {
        this.loader = loader;
    }

    public boolean isFirstLaunch() {return firstLaunch;}

    public void setFirstLaunch(boolean firstLaunch) {this.firstLaunch = firstLaunch;}
}





