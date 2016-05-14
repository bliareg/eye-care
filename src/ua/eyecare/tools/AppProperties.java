package ua.eyecare.tools;

import sun.nio.cs.ext.MacUkraine;
import ua.eyecare.model.Localizable;

import java.beans.PropertyChangeEvent;
import java.io.*;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Created by bliareg on 30.06.15.
 */
public class AppProperties{

    public enum PROPERTIES{LANGUAGE,DELAY,PERIOD,TYPE_OF_WINDOW,POSITION_X,POSITION_Y,SHOWING_TIME,HIDING_TIME,FIRST_LAUNCH}
    public enum LANGUAGES{English,Українська}

    private Properties props;
    private String configFilePath;
    private Set<Localizable> lobjs;




    public AppProperties(String configFilePath) throws IOException, BackingStoreException {
        props = new Properties();
        this.configFilePath = configFilePath;


        File file = new File(configFilePath);
        if(file.exists()){
            load();

        }else{
            file.createNewFile();
            setDefaultValue();
            props.setProperty(PROPERTIES.FIRST_LAUNCH.toString(),"true");
            save();
        }
    }


    public void setDefaultValue() throws BackingStoreException, IOException {
            props.setProperty(PROPERTIES.LANGUAGE.toString(), "en");
            props.setProperty(PROPERTIES.DELAY.toString(), "0");
            props.setProperty(PROPERTIES.PERIOD.toString(), "1000");
            props.setProperty(PROPERTIES.SHOWING_TIME.toString(),"60");
            props.setProperty(PROPERTIES.HIDING_TIME.toString(),"5400");
            props.setProperty(PROPERTIES.TYPE_OF_WINDOW.toString(), "small");
            props.setProperty(PROPERTIES.POSITION_X.toString(), "500");
            props.setProperty(PROPERTIES.POSITION_Y.toString(), "500");
    }


    public void setProperty(PROPERTIES key, Object value){
        if(key.equals(PROPERTIES.LANGUAGE))
            if(!getProperty(PROPERTIES.LANGUAGE).equals(key))
                langWasChanged();

        props.setProperty(String.valueOf(key), String.valueOf(value));
    }


    public String getProperty(PROPERTIES key){
        return String.valueOf(props.get(String.valueOf(key)));}
    public int getPropertyInteger(PROPERTIES key){
        return Integer.parseInt(props.getProperty(String.valueOf(key)));}
    public double getPropertyDouble(PROPERTIES key){
        return Double.parseDouble(props.getProperty(String.valueOf(key)));
    }
    public boolean getPropertyBoolean(PROPERTIES key){
        return new Boolean(props.getProperty(String.valueOf(key)));
    }


    public void save() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(configFilePath));
        props.store(writer, "Eye Care configuration file");
        writer.close();
    }
    public void load() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(configFilePath));
        props.load(reader);
        reader.close();
    }
    private void onDestroy(){
        try {
            save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void setLobjs(Set<Localizable> lobjs) {
        this.lobjs = lobjs;
    }
    private void langWasChanged(){

        Iterator<Localizable> i = lobjs.iterator();

        while(i.hasNext())
            i.next().languageWasChanged();




    }
}
