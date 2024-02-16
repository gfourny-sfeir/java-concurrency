package fr.java.concurrency.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import fr.java.concurrency.model.Dilly;

/**
 * @author gfourny
 */
public class VirtualThread {

    private final Apis apis;

    public VirtualThread() {
        this.apis = new Apis();
    }

    public Dilly async() {

        try (var executors = Executors.newVirtualThreadPerTaskExecutor()) {

            var preferencesFuture = executors.submit(apis::fetchPreferences);
            var beerFuture = executors.submit(() -> apis.fetchBeer(preferencesFuture.get()));
            var vodkaFuture = executors.submit(apis::fetchVodka);

            return new Dilly(beerFuture.get(), vodkaFuture.get());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
