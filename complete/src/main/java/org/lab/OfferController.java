package org.lab;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.lab.services.OfferService;
import org.lab.util.OfferUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Offer controller for the RESTful service.
 */
@RestController
@Api(value = "offer-controller", description = "Offer RESTful API")
public class OfferController {

    private OfferService offerService;

    @Autowired
    public void setOfferService(OfferService offerService) {
        this.offerService = offerService;
    }

    @RequestMapping(value = "/offer", method = POST)
    @ApiOperation(value = "Add an offer")
    public ResponseEntity addOffer(@RequestBody Offer offer) {
        if (!OfferUtils.isValid(offer)) {
            return new ResponseEntity("Offer is invalid", HttpStatus.BAD_REQUEST);
        }
        offer.setId(OfferUtils.getIncrementalId());
        offerService.saveOffer(offer);
        return new ResponseEntity(offer, HttpStatus.OK);
    }

    @RequestMapping(value = "/offer/{id}", method = PUT)
    @ApiOperation(value = "Update an offer with a given id")
    public ResponseEntity updateOffer(@PathVariable Integer id, @RequestBody Offer offer) {
        if (id == null || id < 1) {
            return new ResponseEntity("Invalid id " + id, HttpStatus.BAD_REQUEST);
        }
        if (!OfferUtils.isStorable(offer)) {
            return new ResponseEntity("Offer is invalid", HttpStatus.BAD_REQUEST);
        }
        final Offer storedOffer = offerService.getOfferById(id);
        if (storedOffer == null) {
            return new ResponseEntity("No offer found with id " + id, HttpStatus.BAD_REQUEST);
        }
        final Offer resultOffer = offerService.saveOffer(offer);
        return new ResponseEntity(resultOffer, HttpStatus.OK);
    }

    @RequestMapping(value = "/offer/{id}", method = GET)
    @ApiOperation(value = "Get an offer with a given id")
    public ResponseEntity getOffers(@PathVariable Integer id) {
        if (id == null || id < 1) {
            return new ResponseEntity("Invalid id " + id, HttpStatus.BAD_REQUEST);
        }
        Offer offer = offerService.getOfferById(id);
        if (offer == null) {
            return new ResponseEntity("No offer found with id " + id, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(offer, HttpStatus.OK);
    }

    @RequestMapping(value = "/offer/{id}", method = DELETE)
    @ApiOperation(value = "Delete an offer with a given id")
    public ResponseEntity delete(@PathVariable Integer id) {
        if (id == null || id < 1) {
            return new ResponseEntity("Invalid id " + id, HttpStatus.BAD_REQUEST);
        }
        try {
            offerService.deleteOffer(id);
            return new ResponseEntity("Offer with id " + id + " deleted successfully", HttpStatus.OK);
        } catch (final EmptyResultDataAccessException e) {
            // There was a problem
            return new ResponseEntity("Problem to delete offer with id " + id, HttpStatus.BAD_REQUEST);
        }
    }
}
