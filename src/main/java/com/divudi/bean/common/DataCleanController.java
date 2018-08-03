/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.divudi.bean.common;

import com.divudi.entity.Bill;
import com.divudi.entity.BillComponent;
import com.divudi.entity.BillFee;
import com.divudi.entity.BillFeePayment;
import com.divudi.entity.BillItem;
import com.divudi.entity.BillSession;
import com.divudi.entity.Patient;
import com.divudi.entity.PatientItem;
import com.divudi.entity.Person;
import com.divudi.entity.lab.PatientInvestigation;
import com.divudi.entity.lab.PatientReport;
import com.divudi.entity.lab.PatientReportItemValue;
import com.divudi.facade.BatchBillFacade;
import com.divudi.facade.BillComponentFacade;
import com.divudi.facade.BillEntryFacade;
import com.divudi.facade.BillFacade;
import com.divudi.facade.BillFeeFacade;
import com.divudi.facade.BillFeePaymentFacade;
import com.divudi.facade.BillItemFacade;
import com.divudi.facade.BillSessionFacade;
import com.divudi.facade.PatientEncounterFacade;
import com.divudi.facade.PatientFacade;
import com.divudi.facade.PatientInvestigationFacade;
import com.divudi.facade.PatientItemFacade;
import com.divudi.facade.PatientReportFacade;
import com.divudi.facade.PatientReportItemValueFacade;
import com.divudi.facade.PatientRoomFacade;
import com.divudi.facade.PaymentFacade;
import com.divudi.facade.PersonFacade;
import com.divudi.facade.PharmaceuticalBillItemFacade;
import com.divudi.facade.StockFacade;
import com.divudi.facade.StockHistoryFacade;
import com.divudi.facade.util.JsfUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author buddhika_ari
 */
@Named(value = "dataCleanController")
@ApplicationScoped
public class DataCleanController {

    @EJB
    private BillFacade billFacade;
    @EJB
    private BillItemFacade billItemFacade;
    @EJB
    private BillFeeFacade billFeeFacade;
    @EJB
    private BillComponentFacade billComponentFacade;
    @EJB
    private BillEntryFacade billEntryFacade;
    @EJB
    private BatchBillFacade batchBillFacade;
    @EJB
    private BillFeePaymentFacade billFeePaymentFacade;
    @EJB
    private BillSessionFacade billSessionFacade;
    @EJB
    private PatientFacade patientFacade;
    @EJB
    private PatientEncounterFacade patientEncounterFacade;
    @EJB
    private PatientInvestigationFacade patientInvestigationFacade;
    @EJB
    private PatientItemFacade patientItemFacade;
    @EJB
    private PatientReportFacade patientReportFacade;
    @EJB
    private PatientReportItemValueFacade patientReportItemValueFacade;
    @EJB
    private PatientRoomFacade patientRoomFacade;
    @EJB
    private PaymentFacade paymentFacade;
    @EJB
    private PersonFacade personFacade;
    @EJB
    private PharmaceuticalBillItemFacade pharmaceuticalBillItemFacade;
    @EJB
    private StockFacade stockFacade;
    @EJB
    private StockHistoryFacade stockHistoryFacade;

    private Long deletedRecordCount = 0l;
    private String currentBlock;
    private Date fromDate;
    private Date toDate;

    public void deleteRecords() {
        currentBlock = "Deleting";
        deletedRecordCount = 0l;
        String j;
        Map m;
        j = "select b from Bill b where b.createdAt between :fd and :td";
        m = new HashMap();
        m.put("fd", fromDate);
        System.out.println("fromDate = " + fromDate);

        m.put("td", toDate);
        System.out.println("toDate = " + toDate);

        List<Bill> bills = getBillFacade().findBySQL(j, m);
        currentBlock = "Bill";
        for (Bill b : bills) {
            System.out.println("b = " + b);
            b.setRetired(true);
            getBillFacade().edit(b);
            deletedRecordCount++;
            Patient p = b.getPatient();
            if (p != null) {
                p.setRetired(true);
                getPatientFacade().edit(p);
                deletedRecordCount++;
                Person ps = p.getPerson();
                if (ps != null) {
                    ps.setRetired(true);
                    getPersonFacade().edit(ps);
                }
            }
            deletedRecordCount++;
        }
        currentBlock = "BillItem";
        j = "select b from BillItem b where b.createdAt between :fd and :td";
        List<BillItem> billItems = getBillItemFacade().findBySQL(j, m);
        for (BillItem bi : billItems) {
            System.out.println("bi = " + bi);
            bi.setRetired(true);
            billItemFacade.edit(bi);
            deletedRecordCount++;
        }
        currentBlock = "BillComponent";
        j = "select b from BillComponent b where b.createdAt between :fd and :td";
        List<BillComponent> billComponents = getBillComponentFacade().findBySQL(j, m);
        for (BillComponent bi : billComponents) {
            System.out.println("bi = " + bi);
            bi.setRetired(true);
            getBillComponentFacade().edit(bi);
            deletedRecordCount++;
        }
        currentBlock = "BillFee";
        j = "select b from BillFee b where b.createdAt between :fd and :td";
        List<BillFee> billEntries = getBillFeeFacade().findBySQL(j, m);
        for (BillFee bi : billEntries) {
            System.out.println("bi = " + bi);
            bi.setRetired(true);
            getBillFeeFacade().edit(bi);
            deletedRecordCount++;
        }
        currentBlock = "BillFeePayment";
        j = "select b from BillFeePayment b where b.createdAt between :fd and :td";
        List<BillFeePayment> billFeePayments = getBillFeePaymentFacade().findBySQL(j, m);
        for (BillFeePayment bi : billFeePayments) {
            System.out.println("bi = " + bi);
            bi.setRetired(true);
            getBillFeePaymentFacade().edit(bi);
            deletedRecordCount++;
        }
        currentBlock = "BillSession";
        j = "select b from BillSession b where b.createdAt between :fd and :td";
        List<BillSession> billSessions = getBillSessionFacade().findBySQL(j, m);
        for (BillSession bi : billSessions) {
            System.out.println("bi = " + bi);
            bi.setRetired(true);
            getBillSessionFacade().edit(bi);
            deletedRecordCount++;
        }
        currentBlock = "PatientInvestigation";
        j = "select b from PatientInvestigation b where b.createdAt between :fd and :td";
        List<PatientInvestigation> patientInvestigations = getPatientInvestigationFacade().findBySQL(j, m);
        for (PatientInvestigation bi : patientInvestigations) {
            System.out.println("bi = " + bi);
            bi.setRetired(true);
            getPatientInvestigationFacade().edit(bi);
            deletedRecordCount++;
        }
        currentBlock = "PatientItem";
        j = "select b from PatientItem b where b.createdAt between :fd and :td";
        List<PatientItem> patientItems = getPatientItemFacade().findBySQL(j, m);
        for (PatientItem bi : patientItems) {
            System.out.println("bi = " + bi);
            bi.setRetired(true);
            getPatientItemFacade().edit(bi);
            deletedRecordCount++;
        }
        currentBlock = "PatientReport";
        j = "select b from PatientReport b where b.createdAt between :fd and :td";
        List<PatientReport> patientReports = getPatientReportFacade().findBySQL(j, m);
        for (PatientReport bi : patientReports) {
            System.out.println("bi = " + bi);
            bi.setRetired(true);
            getPatientReportFacade().edit(bi);
            deletedRecordCount++;
            j = "select b from PatientReportItemValue b where b.patientReport =:pr";
            m = new HashMap();
            m.put("pr", bi);
            PatientReportItemValue v = getPatientReportItemValueFacade().findFirstBySQL(j, m);
            getPatientReportItemValueFacade().remove(v);
            deletedRecordCount++;
        }
        currentBlock = "PatientReportItemValue";

        JsfUtil.addSuccessMessage("Deleted");
    }

    /**
     * Creates a new instance of DataCleanController
     */
    public DataCleanController() {
    }

    public Long getDeletedRecordCount() {
        return deletedRecordCount;
    }

    public void setDeletedRecordCount(Long deletedRecordCount) {
        this.deletedRecordCount = deletedRecordCount;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public BillFacade getBillFacade() {
        return billFacade;
    }

    public BillItemFacade getBillItemFacade() {
        return billItemFacade;
    }

    public BillFeeFacade getBillFeeFacade() {
        return billFeeFacade;
    }

    public BillComponentFacade getBillComponentFacade() {
        return billComponentFacade;
    }

    public BillEntryFacade getBillEntryFacade() {
        return billEntryFacade;
    }

    public BatchBillFacade getBatchBillFacade() {
        return batchBillFacade;
    }

    public BillFeePaymentFacade getBillFeePaymentFacade() {
        return billFeePaymentFacade;
    }

    public BillSessionFacade getBillSessionFacade() {
        return billSessionFacade;
    }

    public PatientFacade getPatientFacade() {
        return patientFacade;
    }

    public PatientEncounterFacade getPatientEncounterFacade() {
        return patientEncounterFacade;
    }

    public PatientInvestigationFacade getPatientInvestigationFacade() {
        return patientInvestigationFacade;
    }

    public PatientItemFacade getPatientItemFacade() {
        return patientItemFacade;
    }

    public PatientReportFacade getPatientReportFacade() {
        return patientReportFacade;
    }

    public PatientReportItemValueFacade getPatientReportItemValueFacade() {
        return patientReportItemValueFacade;
    }

    public PatientRoomFacade getPatientRoomFacade() {
        return patientRoomFacade;
    }

    public PaymentFacade getPaymentFacade() {
        return paymentFacade;
    }

    public PersonFacade getPersonFacade() {
        return personFacade;
    }

    public PharmaceuticalBillItemFacade getPharmaceuticalBillItemFacade() {
        return pharmaceuticalBillItemFacade;
    }

    public StockFacade getStockFacade() {
        return stockFacade;
    }

    public StockHistoryFacade getStockHistoryFacade() {
        return stockHistoryFacade;
    }

    public String getCurrentBlock() {
        return currentBlock;
    }

    public void setCurrentBlock(String currentBlock) {
        this.currentBlock = currentBlock;
    }

}
