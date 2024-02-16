package fr.java.concurrency.service;

import fr.java.concurrency.model.Dilly;
import fr.java.concurrency.model.DillyMutable;

/**
 * @author gfourny
 */

public class Threads implements Caller {

    private final Apis apis;

    public Threads() {
        this.apis = new Apis();
    }

    @Override
    public Dilly async() {
        var dillyMutable = new DillyMutable();
        var pref = apis.fetchPreferences();
        var t1 = new Thread(() -> dillyMutable.setBeer(apis.fetchBeer(pref)));
        var t2 = new Thread(() -> dillyMutable.setVodka(apis.fetchVodka()));
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return new Dilly(dillyMutable.getBeer(), dillyMutable.getVodka());
    }
    // === PREHISTORIC ===
    // ❌ Wasteful: creates new threads for each request
    // ❌ Mutates state: risk of race bugs / deadlocks protecting the mutations

}
