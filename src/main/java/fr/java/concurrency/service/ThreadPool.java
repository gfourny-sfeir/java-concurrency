package fr.java.concurrency.service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.java.concurrency.model.Dilly;
import fr.java.concurrency.model.DillyMutable;

/**
 * @author gfourny
 */

public class ThreadPool implements Caller {

    private final Apis apis;

    private final ExecutorService executor = Executors.newFixedThreadPool(200);

    public ThreadPool() {
        this.apis = new Apis();
    }

    @Override
    public Dilly async() {
        var dillyMutable = new DillyMutable();

        var done = new CountDownLatch(2);

        var pref = apis.fetchPreferences();

        // i await for 2 responses

        executor.execute(() -> {
            dillyMutable.setBeer(apis.fetchBeer(pref));
            done.countDown();
        });
        executor.execute(() -> {
            dillyMutable.setVodka(apis.fetchVodka());
            done.countDown();
        });
        try {
            done.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Dilly(dillyMutable.getBeer(), dillyMutable.getVodka());
    }
    // === ANCIENT ===
    // ✅ Reuses expensive threads
    // ❌ Mutates state: risk of race bugs / deadlocks protecting the mutations
}
