/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lab;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab.services.OfferService;
import org.lab.util.OfferUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests on the OfferController.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OfferControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OfferService offerService;

    @InjectMocks
    private OfferController offerController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(offerController)
                .build();
    }

    /**
     * Converts a Java object into JSON representation.
     *
     * @param obj the object to convert.
     * @return a Java object into JSON representation.
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void addOfferWithSuccess() throws Exception {
        final Offer offer = OfferUtils.getDefaultOffer();
        when(offerService.saveOffer(any())).thenReturn(offer);
        this.mockMvc.perform(
                post("/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(offer)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(10));
    }

    @Test
    public void updateOfferWithSuccess() throws Exception {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(OfferUtils.getIncrementalId());
        offer.setProduct("smart");
        when(offerService.getOfferById(offer.getId())).thenReturn(offer);
        when(offerService.saveOffer(any())).thenReturn(offer);
        mockMvc.perform(
                put("/offer/{id}", offer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(offer)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.product").value("smart"));
    }

    @Test
    public void getOfferWithSuccess() throws Exception {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(new Integer(8));
        when(offerService.getOfferById(offer.getId())).thenReturn(offer);
        mockMvc.perform(
                get("/offer/{id}", offer.getId()))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(10));
    }


    @Test
    public void deleteOfferWithSuccess() throws Exception {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(new Integer(8));
        OfferService spy = spy(offerService);
        doNothing().when(spy).deleteOffer(offer.getId());
        mockMvc.perform(
                delete("/offer/{id}", offer.getId()))
                .andDo(print()).andExpect(status().isOk());
    }

    @Test
    public void addOfferWithErrorDueToInvalidEntry() throws Exception {
        final Offer offer = OfferUtils.getDefaultOffer();
        // negative price not allowed
        offer.setPrice(new BigDecimal("-200"));
        this.mockMvc.perform(
                post("/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(offer)))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void updateOfferWithErrorDueToMissingEntry() throws Exception {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(OfferUtils.getIncrementalId());
        when(offerService.getOfferById(offer.getId())).thenReturn(null);
        when(offerService.saveOffer(any())).thenReturn(offer);
        mockMvc.perform(
                put("/offer/{id}", offer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(offer)))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void updateOfferWithErrorDueToInvalidEntry() throws Exception {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(OfferUtils.getIncrementalId());
        // wrong period of validity
        offer.setEnd(offer.getStart().minusDays(20));
        when(offerService.getOfferById(offer.getId())).thenReturn(offer);
        when(offerService.saveOffer(any())).thenReturn(offer);
        mockMvc.perform(
                put("/offer/{id}", offer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(offer)))
                .andDo(print()).andExpect(status().isBadRequest());
    }


    @Test
    public void updateOfferWithErrorDueToInvalidId() throws Exception {
        // negative id not allowed
        final int id = -1;
        final Offer offer = OfferUtils.getDefaultOffer();
        mockMvc.perform(
                put("/offer/{id}", -1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(offer)))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteOfferWithErrorDueToInvalidId() throws Exception {
        // negative id not allowed
        final int id = -1;
        mockMvc.perform(
                delete("/offer/{id}", id))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void deleteOfferWithErrorDueNonExistingId() throws Exception {
        doThrow(EmptyResultDataAccessException.class)
                .when(offerService)
                .deleteOffer(any());
        mockMvc.perform(
                delete("/offer/{id}", 1000))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void getOfferWithErrorDueToInvalidId() throws Exception {
        // negative id not allowed
        final int id = -1;
        mockMvc.perform(
                get("/offer/{id}", id))
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    public void getOfferWithErrorDueToNonExistingId() throws Exception {
        final Offer offer = OfferUtils.getDefaultOffer();
        offer.setId(new Integer(8));
        when(offerService.getOfferById(offer.getId())).thenReturn(null);
        mockMvc.perform(
                get("/offer/{id}", offer.getId()))
                .andDo(print()).andExpect(status().isBadRequest());
    }
}
