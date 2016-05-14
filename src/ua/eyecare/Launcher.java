package ua.eyecare;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.eyecare.controller.MainApp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by bliareg on 07.07.15.
 */
public abstract class Launcher {

    private static ConfigurableApplicationContext cctx;


    public static void main(String[] args) {
        MainApp.createMainApp(args);
    }

    public static void launchContext(){
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("ua/eyecare/controller/config.xml");
        cctx = (ConfigurableApplicationContext) ctx;
        MainApp m = (MainApp) ctx.getBean("mainApp");

            writeVersion((String) ctx.getBean("version"));

        m.startApp();
    }

    private static class launchLater extends Thread{

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                launchContext();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public static void shutDownApp(){
        cctx.close();
    }


    private static void writeVersion(String version){
        File f = new File("version");
        if(!f.exists()){
            try {
                f.createNewFile();
                FileWriter w = new FileWriter(f);
                w.write(version);
                w.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
