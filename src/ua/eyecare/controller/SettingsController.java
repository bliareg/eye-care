package ua.eyecare.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import ua.eyecare.model.Localizable;
import ua.eyecare.tools.AppProperties;
import ua.eyecare.tools.StringLoader;
import ua.eyecare.tools.TimeFormat;

import java.io.IOException;
import java.util.Set;
import java.util.prefs.BackingStoreException;

/**
 * Created by bliareg on 12.07.15.
 */
public class SettingsController implements Localizable{


    @FXML private Label lang;
//    @FXML private Label window;
    @FXML private Label showingTime;
    @FXML private Label hidingTime;
    @FXML private Label choosedHidingTime;
    @FXML private Label choosedShowingTime;

//    @FXML private ComboBox windowCB;
    @FXML private ComboBox langCB;

    @FXML private Slider showingTimeSlider;
    @FXML private Slider hidingTimeSlider;

    @FXML private Button defaultButton;

    private StringLoader loader;
    private AppProperties properties;

    private ObservableList<String> windowsTypes;
    private ObservableList<String> languages;
    private Set<Localizable> localizables;
    private MainApp.EyeCareTimerTask t;

    @Override
    public void languageWasChanged() {
        initAllStringResources();
    }

    @FXML
    private void initialize(){
            InitLater ilt = new InitLater();
        ilt.start();
    }



    public void handleDefault(){

    }

    private void firstInitAllStringResources(){
        lang.setText(loader.getString("ST_labelLanguage"));
//        window.setText(loader.getString("ST_labelWindow"));
        showingTime.setText(loader.getString("ST_labelShowingTime"));
        hidingTime.setText(loader.getString("ST_labelHidingTime"));
        defaultButton.setText(loader.getString("ST_buttonDefault"));
    }

    private void initAllStringResources(){
        lang.setText(loader.getString("ST_labelLanguage"));
//        window.setText(loader.getString("ST_labelWindow"));
        showingTime.setText(loader.getString("ST_labelShowingTime"));
        hidingTime.setText(loader.getString("ST_labelHidingTime"));
        defaultButton.setText(loader.getString("ST_buttonDefault"));

//        windowsTypes.clear();
//        windowsTypes.add(loader.getString("ST_CB_WindowElementS"));
//        windowsTypes.add(loader.getString("ST_CB_WindowElementL"));
//            String wt = properties.getProperty(AppProperties.PROPERTIES.TYPE_OF_WINDOW);
//        if(wt.equals("small")){windowCB.getSelectionModel().select(0);}else if(wt.equals("large")){windowCB.getSelectionModel().select(1);}

    }

    private void initElements(){

         languages = FXCollections.observableArrayList();
        for(AppProperties.LANGUAGES l : AppProperties.LANGUAGES.values())
        languages.add(String.valueOf(l));

        windowsTypes = FXCollections.observableArrayList();
        windowsTypes.add(loader.getString("ST_CB_WindowElementS"));
        windowsTypes.add(loader.getString("ST_CB_WindowElementL"));

//        windowCB.setItems(windowsTypes);
        langCB.setItems(languages);


        hidingTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                formatChoosedHidingTime();

                properties.setProperty(AppProperties.PROPERTIES.HIDING_TIME, (int) (hidingTimeSlider.getValue() * 60));
                t.waitNow();
                t.unexpectedlyChangeTime();

            }
        });

        showingTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                formatChoosedShowingTime();

                properties.setProperty(AppProperties.PROPERTIES.SHOWING_TIME, (int) showingTimeSlider.getValue());
                t.waitNow();
                t.unexpectedlyChangeTime();
            }
        });

//        windowCB.valueProperty().addListener(new ChangeListener() {
//            @Override
//            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//
//            }
//        });
        langCB.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {

                if(langCB.getValue().equals(String.valueOf(AppProperties.LANGUAGES.English))) {
                    properties.setProperty(AppProperties.PROPERTIES.LANGUAGE, "en");

                        try {properties.save();} catch (IOException e) {e.printStackTrace();}

                }else{
                    if(langCB.getValue().equals(String.valueOf(AppProperties.LANGUAGES.Українська))){
                        properties.setProperty(AppProperties.PROPERTIES.LANGUAGE, "ua");

                        try {properties.save();} catch (IOException e) {e.printStackTrace();}

                    }
                }

//                switch (langCB.getValue()){
//                    case String.valueOf(AppProperties.LANGUAGES.English) : properties.setProperty(AppProperties.PROPERTIES.LANGUAGE, "en");
//                        break;
//                    case String.valueOf(AppProperties.LANGUAGES.Українська) :  properties.setProperty(AppProperties.PROPERTIES.LANGUAGE, "ua"); break;
//                }


                loader.languageWasChanged();
                for(Localizable l:localizables){
                    l.languageWasChanged();
                }
                languageWasChanged();
            }
        });

        hidingTimeSlider.setValue(properties.getPropertyDouble((AppProperties.PROPERTIES.HIDING_TIME)) / 60);
        showingTimeSlider.setValue(properties.getPropertyDouble((AppProperties.PROPERTIES.SHOWING_TIME)));


        switch (properties.getProperty(AppProperties.PROPERTIES.LANGUAGE)){
            case "en" :  langCB.getSelectionModel().select(String.valueOf(AppProperties.LANGUAGES.English)); break;
            case "ua" :  langCB.getSelectionModel().select(String.valueOf(AppProperties.LANGUAGES.Українська)); break;
        }

//        switch(properties.getProperty(AppProperties.PROPERTIES.TYPE_OF_WINDOW)){
//            case "small" : windowCB.getSelectionModel().select(0); break;
//            case "large" : windowCB.getSelectionModel().select(1); break;
//        }

        defaultButton.setOnAction(event->{
            try {   properties.setDefaultValue();   } catch (BackingStoreException e) {e.printStackTrace();} catch (IOException e) {e.printStackTrace();}
            try {   properties.save();   } catch (IOException e) {e.printStackTrace();}
            langCB.getSelectionModel().select(0);

            loader.languageWasChanged();
            for(Localizable l:localizables){
                l.languageWasChanged();
            }
            languageWasChanged();
            t.waitNow();
            t.unexpectedlyChangeTime();
        });

        formatChoosedHidingTime();
        formatChoosedShowingTime();
    }

    private void formatChoosedHidingTime(){
        choosedHidingTime.setText(TimeFormat.convertTimeToView(((int) (hidingTimeSlider.getValue() * 60))));}
    private void formatChoosedShowingTime(){
        choosedShowingTime.setText(TimeFormat.convertTimeToView(((int) showingTimeSlider.getValue())));}

    public void setProperties(AppProperties properties) {
        this.properties = properties;
    }
    public void setLoader(StringLoader loader) {
        this.loader = loader;
    }
    public void setLocalizables(Set<Localizable> localizables) {
        this.localizables = localizables;
    }

    private class InitLater extends Thread{
        @Override
        public void run() {
            while(loader==null){
                try {Thread.sleep(75);} catch (InterruptedException e) {e.printStackTrace();}}
            firstInitAllStringResources();
            initElements();

        }
    }

    public void setT(MainApp.EyeCareTimerTask t) {
        this.t = t;
    }
}
