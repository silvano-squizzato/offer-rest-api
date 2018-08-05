package org.lab.services;

import org.lab.Offer;

/**
 * Interface for the service managing persistence.
 */
public interface OfferService {
    Iterable<Offer> listAllOffers();

    Offer getOfferById(Integer id);

    Offer saveOffer(Offer offer);

    void deleteOffer(Integer id);
}
