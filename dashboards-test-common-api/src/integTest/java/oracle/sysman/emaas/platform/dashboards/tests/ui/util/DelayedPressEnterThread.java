package oracle.sysman.emaas.platform.dashboards.tests.ui.util;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by shjie on 2016/4/8.
 */
public class DelayedPressEnterThread implements Runnable{
    Thread runner;
    private int delay ;

    public DelayedPressEnterThread() {
    }

    public DelayedPressEnterThread(String threadName) {
        this.delay = 5000 ;
        init (threadName) ;
    }

    public DelayedPressEnterThread(String threadName, int delay) {
        this.delay = delay ;
        init (threadName) ;
    }
    private void init(String threadName) {
        runner = new Thread(this, threadName); // (1) Create a new thread.
        runner.start(); // (2) Start the thread.
    }
    public void run() {
        Robot robot = null ;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        robot.delay(delay) ;
        robot.keyPress(KeyEvent.VK_ESCAPE) ;
        robot.keyRelease(KeyEvent.VK_ESCAPE) ;
    }

}

