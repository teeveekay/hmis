/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.data.dataStructure;

import com.divudi.entity.Institution;
import com.divudi.entity.Item;

/**
 *
 * @author buddhika_ari
 */
public class BillItemSummery {
    Item item;
    Institution institution;
    Long count;
    Double grossValue;
    Double discountValue;
    Double netValue;

    public BillItemSummery() {
    }

    public BillItemSummery(Item item, Institution institution, Long count, Double grossValue, Double discountValue, Double netValue) {
        this.item = item;
        this.institution = institution;
        this.count = count;
        this.grossValue = grossValue;
        this.discountValue = discountValue;
        this.netValue = netValue;
    }

    
    
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Double getGrossValue() {
        return grossValue;
    }

    public void setGrossValue(Double grossValue) {
        this.grossValue = grossValue;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Double getNetValue() {
        return netValue;
    }

    public void setNetValue(Double netValue) {
        this.netValue = netValue;
    }
    
    
    
}
