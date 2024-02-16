package fr.java.concurrency.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import fr.java.concurrency.model.Dilly;

/**
 * @author gfourny
 */
public class CompletableFutureWithVT implements Caller {

    private final Apis apis;

    public CompletableFutureWithVT() {
        this.apis = new Apis();
    }

    @Override
    public Dilly async() {

        try (var executors = Executors.newVirtualThreadPerTaskExecutor()) {

            return CompletableFuture.supplyAsync(apis::fetchPreferences, executors)
                    .thenApply(apis::fetchBeer)
                    .thenCombine(CompletableFuture.supplyAsync(apis::fetchVodka, executors), Dilly::new)
                    .join();
        }
    }
}
