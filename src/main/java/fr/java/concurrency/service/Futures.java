package fr.java.concurrency.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import fr.java.concurrency.model.Dilly;

/**
 * @author gfourny
 */

public class Futures implements Caller {

    private final Apis apis;

    public Futures() {
        this.apis = new Apis();
    }

    @Override
    public Dilly async() {

        try (var executors = Executors.newFixedThreadPool(200)) {

            var preferencesFuture = executors.submit(apis::fetchPreferences);
            var beerFuture = executors.submit(() -> apis.fetchBeer(preferencesFuture.get()));
            var vodkaFuture = executors.submit(apis::fetchVodka);

            return new Dilly(beerFuture.get(), vodkaFuture.get());

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
