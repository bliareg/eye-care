package ua.eyecare.tools;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bliareg on 29.06.15.
 */
public class EyeCareTimer extends Timer{

    private int delay;
    private int period;
    private AppProperties props;
    private TimerTask task;

    public EyeCareTimer(AppProperties props) {
        this.props = props;

        delay = props.getPropertyInteger(AppProperties.PROPERTIES.DELAY);
        period = props.getPropertyInteger(AppProperties.PROPERTIES.PERIOD);
    }

    public void start() throws NoneTimerTask {

        if(task==null)
            throw new NoneTimerTask("Set up Timer task!!!");
        else
        shedule();}


    private void shedule(){schedule(task, delay, period);}



    public void voidPrefsChanged(){
        cancel();
        this.shedule();
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public int purge() {
        return super.purge();
    }

    public AppProperties getProps() {
        return props;
    }

    public void setProps(AppProperties props) {
        this.props = props;
    }

    public TimerTask getTask() {
        return task;
    }

    public void setTask(TimerTask task) {
        this.task = task;
    }

    private class NoneTimerTask extends Exception{
        public NoneTimerTask(String message) {
            super(message);
        }
    }
}
