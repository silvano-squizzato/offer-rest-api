package org.lab;

import org.junit.Test;
import org.lab.util.OfferUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests on an offer properties and status.
 */
public class OfferTests {
    Logger log = LoggerFactory.getLogger(OfferTests.class);

    @Test
    public void testValidOffer() {
        final Offer offer = OfferUtils.getDefaultOffer();
        assertTrue(OfferUtils.isValid(offer));
    }

    @Test
    public void testInvalidOfferDueToNegativePrice() {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setPrice(new BigDecimal("-100"));
        assertFalse(OfferUtils.isValid(offer));
    }

    @Test
    public void testInvalidOfferDueToInvalidPeriod() {
        final Offer offer = OfferUtils.getDefaultOffer();
        // negative period
        offer.setEnd(offer.getStart().minusDays(3));
        assertFalse(OfferUtils.isValid(offer));
    }

    @Test
    public void testNotExpiredOffer() {
        final Offer offer = OfferUtils.getDefaultOffer();
        assertFalse(OfferUtils.isExpired(offer));
    }

    @Test
    public void testExpiredOffer() {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setEnd(offer.getStart().minusDays(3));
        assertTrue(OfferUtils.isExpired(offer));
    }

    @Test
    public void testStorableOffer() {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(33);
        assertTrue(OfferUtils.isStorable(offer));
    }

    @Test
    public void testNotStorableOfferDueToInvalidId() {
        final Offer offer = OfferUtils.getDefaultOffer();
        assertFalse(OfferUtils.isStorable(offer));
    }

    @Test
    public void testNotStorableOfferDueToInvalidPeriod() {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(OfferUtils.getIncrementalId());
        offer.setEnd(offer.getStart().minusDays(3));
        assertFalse(OfferUtils.isStorable(offer));
    }

}
