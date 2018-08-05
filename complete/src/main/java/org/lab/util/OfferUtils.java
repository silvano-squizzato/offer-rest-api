package org.lab.util;

import org.lab.Offer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class for offers.
 */
public class OfferUtils {
    private final static Logger LOG = LoggerFactory.getLogger(OfferUtils.class);
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static Integer getIncrementalId() {
        return new Integer(COUNTER.incrementAndGet());
    }

    /**
     * Returns a default offer.
     * The offer is valid for 7 days from now, with a price of 10 and a default product label.
     *
     * @return a default offer.
     */
    public static Offer getDefaultOffer() {
        final Integer id = 0;
        final String product = "Default product";
        final BigDecimal price = new BigDecimal("10.00");
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(7);
        Offer offer = new Offer();
        offer.setId(id);
        offer.setProduct(product);
        offer.setPrice(price);
        offer.setStart(start);
        offer.setEnd(end);
        return offer;
    }

    /**
     * Returns if the offer is valid.
     * An offer is  checked against its constraint annotations and its period of validity.
     *
     * @param offer the offer to validate.
     * @return if the offer is valid.
     */
    public static boolean isValid(Offer offer) {
        boolean result = false;
        if (offer != null) {
            // Javax validation
            final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            final Validator validator = factory.getValidator();
            final Set<ConstraintViolation<Offer>> violations = validator.validate(offer);
            if (violations.isEmpty()) {
                if (isPeriodValid(offer)) {
                    result = true;
                }
            } else {
                for (final ConstraintViolation<Offer> violation : violations) {
                    LOG.info("PropertyPath: " + violation.getPropertyPath() + ", invalid value: " + violation.getInvalidValue() + ", " + violation.getMessage());
                }
            }
        }
        return result;
    }

    /**
     * Returns true if the period of validity (end - start) is non negative.
     *
     * @param offer the offer.
     * @return true if the period of validity (end - start) is non negative.
     */
    public static boolean isPeriodValid(Offer offer) {
        boolean result = false;
        if (offer != null) {
            final LocalDate start = offer.getStart();
            final LocalDate end = offer.getEnd();
            if (start != null && end != null) {
                final Period period = Period.between(start, end);
                if (period.getDays() >= 0) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Returns true if the offer is expired.
     *
     * @param offer the offer.
     * @return true if the offer is expired.
     */
    public static boolean isExpired(Offer offer) {
        boolean result = false;
        if (offer != null) {
            final LocalDate now = LocalDate.now();
            final LocalDate end = offer.getEnd();
            if (end != null) {
                final Period period = Period.between(now, end);
                if (period.getDays() <= 0) {
                    result = true;
                }
            }
        }
        return result;
    }

    /**
     * Returns true if the offer can be saved in the system.
     * Tho offer must be valid and with a valid id.
     *
     * @param offer the offer.
     * @return true if the offer can be saved in the system.
     */
    public static boolean isStorable(Offer offer) {
        boolean result = false;
        if (isValid(offer)) {
            if (offer.getId().intValue() > 0) {
                result = true;
            }
        }
        return result;
    }
}
