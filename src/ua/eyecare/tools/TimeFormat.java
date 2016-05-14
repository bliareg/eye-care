package ua.eyecare.tools;

/**
 * Created by bliareg on 04.07.15.
 */
public abstract class TimeFormat{



    public static String convertTimeToView(int seconds){

        int minutes = 0;
        int hours = 0;

        if(seconds < 0)
            seconds = seconds*(-1);

        if(seconds > 59){
            minutes = seconds/60;
            seconds = seconds - minutes*60 ;
            if(minutes > 59){
                hours = minutes / 60;
                minutes = minutes - hours*60;
                String s = hours+"h."+minutes+"m."+seconds+"s.";
                return s;
            }
            String s = minutes+"m."+seconds+"s.";
            return  s;

        }else {
            return String.valueOf(seconds);
        }
    }


}
