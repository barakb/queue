package com.gigaspaces.lock;

/**
 * Created by Barak Bar Orion
 * on 5/1/16.
 *
 * @since 11.0
 */
public interface RWLock {
    void acquireRead() throws InterruptedException;
    void releaseRead();

    void acquireWrite() throws InterruptedException;
    void releaseWrite();
}
