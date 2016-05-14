package ua.eyecare.tools;

import ua.eyecare.model.Localizable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * Created by bliareg on 09.07.15.
 */
public class StringLoader{

    private ResourceBundle resBundle;
    private AppProperties props;
    private String path;


    public StringLoader(String pathToFiles,AppProperties props) throws IOException {
        this.props = props;
        this.path = pathToFiles;
        loadBundle();

    }

    private void loadBundle() throws IOException {

        resBundle = new PropertyResourceBundle(
                getClass().getResourceAsStream(path+props.
                        getProperty(AppProperties.PROPERTIES.LANGUAGE)));
    }

    public String getString(String key){

        String s = resBundle.getString(key);

        try {
            return  new String(s.getBytes("UTF-32"),"KOI8-U");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return s;
        }
    }




    public void languageWasChanged() {
        try {
            loadBundle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setProps(AppProperties props) {
        this.props = props;
    }
}
