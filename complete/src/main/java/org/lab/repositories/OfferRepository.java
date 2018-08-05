package org.lab.repositories;

import org.lab.Offer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface OfferRepository extends CrudRepository<Offer, Integer> {
}
