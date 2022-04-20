package biz.itonline.test_log.support;

import java.util.Vector;

public class HMObservable {
    private static final String TAG = "HMObservable";
    private boolean changed = false;
    private final Vector<HMObserver> obs;

    /** Construct an Observable with zero HMObservers. */

    public HMObservable() {
        obs = new Vector<>();
    }

    public synchronized void addObserver(HMObserver o) {
        if (o == null)
            throw new NullPointerException();

        String className = o.getClass().toString();
        Vector<HMObserver>  tmpObs = (Vector) obs.clone();
        for (HMObserver hmo: tmpObs
             ) {
            if (hmo.getClass().toString().equals(className)){
                obs.remove(hmo);
            }
        }

        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    public synchronized void deleteObserver(HMObserver o) {
        obs.removeElement(o);
    }

      public void notifyObservers(Object arg) {
        Object[] arrLocal;

        synchronized (this) {
            if (!hasChanged())
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--)
            ((HMObserver)arrLocal[i]).update(this, arg);
    }

    protected synchronized void setChanged() {
        changed = true;
    }

    protected synchronized void clearChanged() {
        changed = false;
    }

    public synchronized boolean hasChanged() {
        return changed;
    }


}
