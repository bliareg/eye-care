package ua.eyecare.model;

import javafx.scene.layout.AnchorPane;
import ua.eyecare.tools.StringLoader;

/**
 * Created by bliareg on 04.07.15.
 */
public interface EyeCare{

    public void setTimerTime(int t);
    public AnchorPane getPane();
    public void setPane(AnchorPane p);
    public void setStringLoader(StringLoader l);
    public void initAllStringResources();
    public void readyToLaunch();


}

