package ua.eyecare.test;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.eyecare.tools.EyeCareTimer;

import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.*;

/**
 * Created by bliareg on 03.07.15.
 */
public class EyeCareTimerTest {

    @Test
    @Before
    public void setUp() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("ua/eyecare/controller/config.xml");

        assertNotNull(ctx.getBean("timer"));
        EyeCareTimer timer = (EyeCareTimer) ctx.getBean("timer");

        timer.setTask(new TestTimerTask());
        timer.start();

    }

    private class TestTimerTask extends TimerTask{

        @Override
        public void run() {
            System.out.println("biiiip-biiiip");
        }
    }

}