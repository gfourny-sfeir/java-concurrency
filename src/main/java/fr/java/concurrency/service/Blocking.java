package fr.java.concurrency.service;

import fr.java.concurrency.model.Dilly;

/**
 * @author gfourny
 */
public class Blocking {

    private final Apis apis;

    public Blocking() {
        this.apis = new Apis();
    }

    public Dilly block() {

        var preferences = apis.fetchPreferences();
        var beer = apis.fetchBeer(preferences);
        var vodka = apis.fetchVodka();

        return new Dilly(beer, vodka);
    }
}
