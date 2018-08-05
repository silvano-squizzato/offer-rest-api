package org.lab;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.configuration.RepositoryConfiguration;
import org.lab.repositories.OfferRepository;
import org.lab.util.OfferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Tests on the repository providing persistence.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RepositoryConfiguration.class})
public class OfferRepositoryTests {
    @Autowired
    private OfferRepository offerRepository;

    @Test
    public void testSaveAndUpdateOffer() {
        // setup offer
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(OfferUtils.getIncrementalId());
        // save offer
        offerRepository.save(offer);
        // fetch from repository
        Offer fetchedOffer = offerRepository.findById(offer.getId()).orElse(null);
        // should not be null
        assertNotNull(fetchedOffer);
        // should equal
        assertEquals(offer.getId(), fetchedOffer.getId());
        assertEquals(offer.getPrice(), fetchedOffer.getPrice());
        // update price and save
        fetchedOffer.setPrice(new BigDecimal(100));
        offerRepository.save(fetchedOffer);
        // get from repository, should be updated
        Offer fetchedUpdatedOffer = offerRepository.findById(fetchedOffer.getId()).orElse(null);
        assertEquals(fetchedUpdatedOffer.getPrice().intValue(), fetchedUpdatedOffer.getPrice().intValue());
        // verify count of products in repository
        final long offerCount = offerRepository.count();
        assertEquals(offerCount, 1);
        // get all offers, list should only have one
        Iterable<Offer> offers = offerRepository.findAll();
        int count = 0;
        for (Offer off : offers) {
            count++;
        }
        assertEquals(count, 1);
    }

    @Test
    public void testFindNonExistentId() {
        Integer id = OfferUtils.getIncrementalId();
        Offer offer = offerRepository.findById(id).orElse(null);
        assertNull(offer);
    }

    @Test
    public void testSaveAndDeleteOffer() {
        // setup offer
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(OfferUtils.getIncrementalId());
        // save offer
        offerRepository.save(offer);
        // fetch from repository
        Offer fetchedOffer = offerRepository.findById(offer.getId()).orElse(null);
        // should not be null
        assertNotNull(fetchedOffer);
        // delete by id
        offerRepository.deleteById(offer.getId());
        fetchedOffer = offerRepository.findById(offer.getId()).orElse(null);
        // should be null
        assertNull(fetchedOffer);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testDeleteNonExistentId() {
        Integer id = OfferUtils.getIncrementalId();
        offerRepository.deleteById(id);
    }
}