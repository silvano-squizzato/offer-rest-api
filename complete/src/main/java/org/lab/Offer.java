package org.lab;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Simple representation of an offer.
 * A valid offer is specific to one product with a given price.
 * Only one implicit currency is used according to the locale.
 * The duration of an offer is given by the difference between the end and start date.
 * In this simple version the granularity of the duration period is in days, and only one timezone is considered.
 * An offer is expired if the current date is beyond the end date.
 * An offer can be tracked through its identifier when stored in the system.
 */
@Entity
public class Offer {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "The offer id")
    private Integer id;

    @NotEmpty
    @ApiModelProperty(required = true, notes = "The product on offer")
    private String product;

    @PositiveOrZero
    @ApiModelProperty(required = true, notes = "The offer price")
    private BigDecimal price;


    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @ApiModelProperty(required = true, notes = "The offer start date (yyyy-MM-dd)")
    private LocalDate start;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @ApiModelProperty(required = true, notes = "The offer end date (yyyy-MM-dd)")
    private LocalDate end;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }
}
