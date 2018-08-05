package org.lab.services;

import org.lab.Offer;
import org.lab.repositories.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the service managing persistence.
 */
@Service
public class OfferServiceImpl implements OfferService {
    private OfferRepository offerRepository;

    @Autowired
    public void setOfferRepository(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    @Override
    public Iterable<Offer> listAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Offer getOfferById(Integer id) {
        return offerRepository.findById(id).orElse(null);
    }

    @Override
    public Offer saveOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    @Override
    public void deleteOffer(Integer id) {
        offerRepository.deleteById(id);
    }
}
