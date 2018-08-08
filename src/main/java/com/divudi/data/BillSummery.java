/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.data;

import com.divudi.entity.WebUser;
import java.util.Date;

/**
 *
 * @author buddhika_ari
 */
public class BillSummery {

    PaymentMethod paymentMethod;
    Double total;
    Double discount;
    Double netTotal;
    Double tax;
    Long count;
    BillType billType;
    WebUser webUser;
    private Long key;
    private BillClassType billClassType;
    private Long id;
    private String insId;
    private String deptId;
    private Date createdAt;
    private String institutionName;
    private String departmentName;
    private String toInstitutionName;
    private String toDepartmentName;
    private String fromInstitutionName;
    private String fromDepartmentName;
    private String name;

    public BillSummery() {
    }

    public BillSummery(PaymentMethod paymentMethod, Double total, Double discount, Double netTotal, Double tax, Long count, BillType billType) {
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.discount = discount;
        this.netTotal = netTotal;
        this.tax = tax;
        this.count = count;
        this.billType = billType;
    }

    public BillSummery(PaymentMethod paymentMethod, BillClassType billClassType, Double total, Double discount, Double netTotal, Double tax, Long count, BillType billType) {
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.discount = discount;
        this.netTotal = netTotal;
        this.tax = tax;
        this.count = count;
        this.billType = billType;
        this.billClassType = billClassType;
    }

    public BillSummery(Long id, String insId, String deptId, Date createdAt,
            String institutionName, String departmentName,
            String toInstitutionName, String toDepartmentName,
            String fromInstitutionName, String fromDepartmentName,
            PaymentMethod paymentMethod, BillClassType billClassType, Double total,
            Double discount, Double netTotal, BillType billType) {
        this.id = id;
        this.insId = insId;
        this.deptId = deptId;
        this.createdAt = createdAt;
        this.institutionName = institutionName;
        this.departmentName = departmentName;
        this.toInstitutionName = toInstitutionName;
        this.toDepartmentName = toDepartmentName;
        this.fromInstitutionName = fromInstitutionName;
        this.fromDepartmentName = fromDepartmentName;
        this.paymentMethod = paymentMethod;
        this.total = total;
        this.discount = discount;
        this.netTotal = netTotal;
        this.billType = billType;
        this.billClassType = billClassType;
    }

    public BillSummery(BillClassType billClassType, String toInstitutionName, String toDepartmentName,
            String fromInstitutionName, String fromDepartmentName,
            Double total, Double discount, Double netTotal
    ) {
        this.billClassType = billClassType;
        this.toInstitutionName = toInstitutionName;
        this.toDepartmentName = toDepartmentName;
        this.fromInstitutionName = fromInstitutionName;
        this.fromDepartmentName = fromDepartmentName;
        this.total = total;
        this.discount = discount;
        this.netTotal = netTotal;
    }

    public BillSummery(String name, Double netTotal
    ) {
        this.name = name;
        this.netTotal = netTotal;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(Double netTotal) {
        this.netTotal = netTotal;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public BillType getBillType() {
        return billType;
    }

    public void setBillType(BillType billType) {
        this.billType = billType;
    }

    public WebUser getWebUser() {
        return webUser;
    }

    public void setWebUser(WebUser webUser) {
        this.webUser = webUser;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public BillClassType getBillClassType() {
        return billClassType;
    }

    public void setBillClassType(BillClassType billClassType) {
        this.billClassType = billClassType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getToInstitutionName() {
        return toInstitutionName;
    }

    public void setToInstitutionName(String toInstitutionName) {
        this.toInstitutionName = toInstitutionName;
    }

    public String getToDepartmentName() {
        return toDepartmentName;
    }

    public void setToDepartmentName(String toDepartmentName) {
        this.toDepartmentName = toDepartmentName;
    }

    public String getFromInstitutionName() {
        return fromInstitutionName;
    }

    public void setFromInstitutionName(String fromInstitutionName) {
        this.fromInstitutionName = fromInstitutionName;
    }

    public String getFromDepartmentName() {
        return fromDepartmentName;
    }

    public void setFromDepartmentName(String fromDepartmentName) {
        this.fromDepartmentName = fromDepartmentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
