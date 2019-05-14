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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

/**
 * This model represents a product.
 *
 * @author Andrei Sidorov
 */
@Entity
@Table(name = "products")
public class Product {

    /**
     * The id of a product.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", unique = true, nullable = false)
    private Long id;

    /**
     * The name of a product.
     */
    @Column(name = "name")
    private String name;

    /**
     * The list of purchasing prices of a product. Keeps track of changes in price.
     */
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private List<Price> purchasingPrices;

    /**
     * The selling price of a product.
     */
    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    /**
     * The category of a product.
     */
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * The atrributes of a product.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "pk.product", cascade = CascadeType.ALL)
    private List<ProductAttribute> attributes;

    /**
     * The weight of a product.
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * The height of a product.
     */
    @Column(name = "height")
    private Double height;

    /**
     * The width of a product.
     */
    @Column(name = "width")
    private Double width;

    /**
     * The depth of a product.
     */
    @Column(name = "depth")
    private Double depth;

    /**
     * The quantity of a product in stock.
     */
    @Column(name = "quantity_in_stock")
    private Integer quantityInStock;

    /**
     * The reserved quantity of a product.
     */
    @Column(name = "quantity_reserved")
    private Integer quantityReserved = 0;

    /**
     * The measure units of a product.
     */
    @ManyToOne
    @JoinColumn(name = "measure_units_id")
    private MeasureUnits measureUnits;

    /**
     * The URL to a product image.
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * The count of how many times a product has been sold.
     */
    @Column(name = "sale_count")
    private Long saleCount;

    /**
     * Returns recent purchsing price of a product.
     *
     * @return the recent purchsing price.
     */
    public BigDecimal getRecentPurchasingPrice() {
        return getPurchasingPrices().get(getPurchasingPrices().size() - 1).getPrice();
    }

    /**
     * Calculates volume of a product by multiplying its height, width and depth.
     *
     * @return the volume of a product.
     */
    public Double calculateVolume() {
        return height * width * depth;
    }

    /* Getters and setters */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Price> getPurchasingPrices() {
        return purchasingPrices;
    }

    public void setPurchasingPrices(List<Price> purchasingPrices) {
        this.purchasingPrices = purchasingPrices;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<ProductAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ProductAttribute> attributes) {
        this.attributes = attributes;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getDepth() {
        return depth;
    }

    public void setDepth(Double depth) {
        this.depth = depth;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public Integer getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(Integer quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

    public MeasureUnits getMeasureUnits() {
        return measureUnits;
    }

    public void setMeasureUnits(MeasureUnits measureUnits) {
        this.measureUnits = measureUnits;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(Long saleCount) {
        this.saleCount = saleCount;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sellingPrice=" + sellingPrice +
                ", weight=" + weight +
                ", height=" + height +
                ", width=" + width +
                ", depth=" + depth +
                ", quantityInStock=" + quantityInStock +
                ", quantityReserved=" + quantityReserved +
                ", imageUrl='" + imageUrl + '\'' +
                ", saleCount=" + saleCount +
                '}';
    }
}





