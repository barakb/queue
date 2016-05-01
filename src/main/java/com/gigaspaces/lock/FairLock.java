package com.gigaspaces.lock;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Barak Bar Orion
 * on 5/1/16.
 *
 * @since 11.0
 */
public class FairLock {
    private List<Thread> waitings;

    public FairLock() {
        waitings = new ArrayList<>();
    }

    public synchronized void acquire() throws InterruptedException {
        waitings.add(Thread.currentThread());
        while(waitings.get(0) != Thread.currentThread()){
            wait();
        }
    }

    public synchronized void release(){
        if(waitings.get(0) == Thread.currentThread()) {
            waitings.remove(0);
            notifyAll();
        }
    }
}
