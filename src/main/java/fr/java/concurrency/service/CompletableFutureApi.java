package fr.java.concurrency.service;

import java.util.concurrent.CompletableFuture;

import fr.java.concurrency.model.Dilly;

/**
 * @author gfourny
 */
public class CompletableFutureApi implements Caller {

    private final Apis apis;

    public CompletableFutureApi() {
        this.apis = new Apis();
    }

    @Override
    public Dilly async() {

        return CompletableFuture.supplyAsync(apis::fetchPreferences)
                .thenApply(apis::fetchBeer)
                .thenCombine(CompletableFuture.supplyAsync(apis::fetchVodka), Dilly::new)
                .join();
    }
}
