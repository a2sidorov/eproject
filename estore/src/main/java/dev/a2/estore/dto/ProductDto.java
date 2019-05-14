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

package dev.a2.estore.dto;

import com.opencsv.bean.CsvBindByName;
import org.hibernate.validator.constraints.NotBlank;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * This class provides the dto of a product.
 *
 * @author Andrei Sidorov
 */
public class ProductDto {

    /**
     * The id of a product.
     */
    @CsvBindByName(column = "ID")
    private Long id;

    /**
     * The name of a product.
     */
    @CsvBindByName(column = "Name")
    @NotBlank
    @Size(max = 70)
    private String name;

    /**
     * The purchasing price of a product.
     */
    @CsvBindByName(column = "Purchasing price")
    @NotNull
    @Min(0)
    private BigDecimal purchasingPrice;

    /**
     * The selling price of a product.
     */
    @CsvBindByName(column = "Selling price")
    @NotNull
    @Min(0)
    private BigDecimal sellingPrice;

    /**
     * The weight of a product.
     */
    @CsvBindByName(column = "Weight")
    @NotNull
    @Min(0)
    private Double weight;

    /**
     * The height of a product.
     */
    @CsvBindByName(column = "Height")
    @NotNull
    @Min(0)
    private Double height;

    /**
     * The width of a product.
     */
    @CsvBindByName(column = "Width")
    @NotNull
    @Min(0)
    private Double width;

    /**
     * The depth of a product.
     */
    @CsvBindByName(column = "Depth")
    @NotNull
    @Min(0)
    private Double depth;

    /**
     * The quantity of a product in stock.
     */
    @CsvBindByName(column = "Quantity in stock")
    @NotNull
    @Min(0)
    private Integer inStock;

    /**
     * The URL to a product image.
     */
    @CsvBindByName(column = "Image url")
    private String imageUrl;

    /**
     * The measure units of a product.
     */
    @CsvBindByName(column = "Measure units id")
    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    private Long measureUnitsId;

    /**
     * The id of a product category.
     */
    @CsvBindByName(column = "Category id")
    @NotNull
    @Min(1)
    @Max(Long.MAX_VALUE)
    private Long categoryId;

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

    public BigDecimal getPurchasingPrice() {
        return purchasingPrice;
    }

    public void setPurchasingPrice(BigDecimal purchasingPrice) {
        this.purchasingPrice = purchasingPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
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

    public Integer getInStock() {
        return inStock;
    }

    public void setInStock(Integer inStock) {
        this.inStock = inStock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getMeasureUnitsId() {
        return measureUnitsId;
    }

    public void setMeasureUnitsId(Long measureUnitsId) {
        this.measureUnitsId = measureUnitsId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", purchasingPrice=" + purchasingPrice +
                ", sellingPrice=" + sellingPrice +
                ", weight=" + weight +
                ", height=" + height +
                ", width=" + width +
                ", depth=" + depth +
                ", inStock=" + inStock +
                ", imageUrl='" + imageUrl + '\'' +
                ", measureUnitsId=" + measureUnitsId +
                ", categoryId=" + categoryId +
                '}';
    }
}

