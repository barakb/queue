package com.gigaspaces.lock;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Barak Bar Orion
 * on 5/1/16.
 *  Disallow Reader + Writer, Writer + Writer
 *  Allow Writer, Readers
 * @since 11.0
 */
@SuppressWarnings("unused")
public class UnfairRWLock implements RWLock {

    private Thread writter;
    private Set<Thread> readers;

    public UnfairRWLock() {
        readers = new HashSet<>();
    }

    @Override
    public synchronized void acquireRead() throws InterruptedException {
        while(writter != null){
            wait();
        }
        readers.add(Thread.currentThread());
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
        while(writter != null || !readers.isEmpty()){
            wait();
        }
        writter = Thread.currentThread();
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
