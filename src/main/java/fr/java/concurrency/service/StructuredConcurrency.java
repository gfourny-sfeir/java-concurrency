package fr.java.concurrency.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredTaskScope;

import fr.java.concurrency.model.Dilly;
import fr.java.concurrency.model.Preferences;

/**
 * @author gfourny
 */
public class StructuredConcurrency {

    private final Apis apis;

    public StructuredConcurrency() {
        this.apis = new Apis();
    }

    public Dilly async() {

        Future<Preferences> preferencesFuture;
        
        try (ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            preferencesFuture = executorService.submit(apis::fetchPreferences);
        }

        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            
            var beerTask = scope.fork(() -> apis.fetchBeer(preferencesFuture.get()));
            var vodkaTask = scope.fork(apis::fetchVodka);

            scope.join().throwIfFailed();

            return new Dilly(beerTask.get(), vodkaTask.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
