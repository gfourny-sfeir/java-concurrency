package fr.java.concurrency.model;

/**
 * @author gfourny
 */
public class DillyMutable {
    private Beer beer;
    private Vodka vodka;

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public Vodka getVodka() {
        return vodka;
    }

    public void setVodka(Vodka vodka) {
        this.vodka = vodka;
    }
}
