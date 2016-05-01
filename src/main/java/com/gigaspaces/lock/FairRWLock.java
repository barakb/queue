package com.gigaspaces.lock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Barak Bar Orion
 * on 5/1/16.
 *  Disallow Reader + Writer, Writer + Writer
 *  Allow Writer, Readers
 * @since 11.0
 */
@SuppressWarnings("unused")
public class FairRWLock implements RWLock {

    private Thread writter;
    private Set<Thread> readers;
    private List<Thread> waitings;


    public FairRWLock() {
        readers = new HashSet<>();
        waitings = new ArrayList<>();
    }

    @Override
    public synchronized void acquireRead() throws InterruptedException {
        waitings.add(Thread.currentThread());
        while(waitings.get(0) != Thread.currentThread() || writter != null){
            wait();
        }
        readers.add(waitings.remove(0));
        notifyAll();
    }

    @Override
    public synchronized void releaseRead() {
        if(!readers.remove(Thread.currentThread())){
            throw new IllegalStateException("Thread not reader");
        }
        notifyAll();
    }

    @Override
    public synchronized void acquireWrite() throws InterruptedException {
        waitings.add(Thread.currentThread());
        while(waitings.get(0) != Thread.currentThread() || writter != null || !readers.isEmpty()){
            wait();
        }
        writter = waitings.remove(0);
    }

    @Override
    public synchronized void releaseWrite() {
        if(writter != Thread.currentThread()){
            throw new IllegalStateException("Thread not writter");
        }
        writter = null;
        notifyAll();
    }
}
