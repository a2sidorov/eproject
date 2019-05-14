/*
 * MIT License
 *
 * Copyright (c) 2019 Andrei Sidorov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.a2.estore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * This model provides relationship betweeen the product model and the attribute model.
 *
 * @author Andrei Sidorov
 */
@Entity
@Table(name = "products_attributes")
public class ProductAttribute implements Serializable {

    /**
     * The primary key of this entity that composed of a product and an attribute.
     */
    @EmbeddedId
    @JsonIgnore
    private ProductAttributePK pk;

    /**
     * The value of an attribute.
     */
    @Column(name = "value")
    private String value;

    /**
     * Constuctor.
     */
    public ProductAttribute() {
        super();
    }

    /**
     * Constructor.
     *
     * @param product a product.
     * @param attribute a product attribute.
     */
    public ProductAttribute(final Product product, final Attribute attribute) {
        pk = new ProductAttributePK();
        pk.setProduct(product);
        pk.setAttribute(attribute);
    }

    /* Getters and setters */

    public ProductAttributePK getPk() {
        return pk;
    }

    public void setPk(ProductAttributePK pk) {
        this.pk = pk;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }



}
