package szoftverfolyamat.osz.game.controller;

import java.util.Timer;
import java.util.TimerTask;
import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptContext;

class JavascriptHost {
    private final ScriptEngine engine;

    public JavascriptHost() {
        ScriptEngineManager sem = new ScriptEngineManager();
        engine = sem.getEngineByName("JavaScript");
    }

    void addReference(String s, Object o) {
        //engine.put(s, o);  // mi a kulonbseg?
        engine.getContext().setAttribute(s, o, ScriptContext.ENGINE_SCOPE);
    }

    void removeReference(String s) {
        engine.getContext().removeAttribute(s, ScriptContext.ENGINE_SCOPE);
    }

    String runScript(String s) {
        String ret = null;
        try {
            engine.eval(s);
        } catch(Exception e) {
            ret = " " + e.toString() + "\n";
            StackTraceElement[] stea = e.getStackTrace();
            for(StackTraceElement ste : stea) {
                ret = ret + "\n " + ste.toString();
            }
        }
        return ret;
    }
}

abstract class Ticker {
    private enum State {
        PRE_START, RUNNING, PAUSED, HALTED
    }

    private Timer timer;
    private State state;
    private int runningTime;
    private long interval;

    public Ticker() {
        timer = new Timer();
        state = State.PRE_START;
    }

    synchronized void start(int secs, long in) {
        if(state==State.PRE_START) {
            interval = in;
            runningTime = (int)(secs*(1000f/interval));
            try {
                TimerTask t =
                    new TimerTask() {
                        public void run() {
                            if(state==State.RUNNING) {
                                runningTime--;
                                if(runningTime<0) {
                                    timeout();
                                    dispose();
                                } else {
                                    tick();
                                }
                            }
                        }
                    };
                timer.schedule(t, 0, interval);
                state = State.RUNNING;
            } catch(Exception e) {
                ;
            }
        }
    }

    synchronized void pause() {
        if(state==State.RUNNING) {
            state = State.PAUSED;
        }
    }

    synchronized void kontinue() {
        if(state==State.PAUSED) {
            state = State.RUNNING;
        }
    }

    synchronized void dispose() {
        state = State.HALTED;
        timer.cancel();
    }

    synchronized void decreaseTime(int secs) {
        runningTime -= secs*(1000f/interval);
        if(runningTime<0) {
            timeout();
            dispose();
        } else {
            tick();
        }
    }

    int getSecondsLeft() {
        return (int)(runningTime/(1000f/interval));
    }

    protected abstract void timeout();

    protected abstract void tick();
}

