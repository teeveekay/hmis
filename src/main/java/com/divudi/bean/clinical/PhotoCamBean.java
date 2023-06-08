/*
 * Open Hospital Management Information System
 * Dr M H B Ariyaratne
 * buddhika.ari@gmail.com
 */
package com.divudi.bean.clinical;

import com.divudi.bean.common.PatientController;
import com.divudi.bean.common.UtilityController;
import com.divudi.data.clinical.ClinicalFindingValueType;
import com.divudi.facade.ClinicalFindingValueFacade;
import com.divudi.facade.util.JsfUtil;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.primefaces.event.CaptureEvent;

/**
 *
 * @author Buddhika
 */
@Named(value = "photoCamBean")
@RequestScoped
public class PhotoCamBean implements Serializable {

    private List<String> photos = new ArrayList<>();

    private String getRandomImageName() {
        int i = (int) (Math.random() * 10000000);

        return String.valueOf(i);
    }

    public List<String> getPhotos() {
        return photos;
    }

    @Inject
    PatientController patientController;
    @Inject
    private PatientEncounterController patientEncounterController;

    @EJB
    private ClinicalFindingValueFacade clinicalFindingValueFacade;

    public PatientController getPatientController() {
        return patientController;
    }

    public void setPatientController(PatientController patientController) {
        this.patientController = patientController;
    }

    public void oncapturePatientPhoto(CaptureEvent captureEvent) {
        if (getPatientController().getCurrent() == null || getPatientController().getCurrent().getId() == null || getPatientController().getCurrent().getId() == 0) {
            UtilityController.addErrorMessage("Patient ?");
            return;
        }
        getPatientController().getCurrent().setBaImage(captureEvent.getData());
        getPatientController().getCurrent().setFileName("patient_image_" + getPatientController().getCurrent().getId() + ".png");
        getPatientController().getCurrent().setFileType("image/png");
        getPatientController().saveSelected();
        UtilityController.addSuccessMessage("Photo captured from webcam.");
    }

    public void oncaptureVisitPhoto(CaptureEvent captureEvent) {
        if (getPatientEncounterController().getCurrent() == null || getPatientEncounterController().getCurrent().getId() == null) {
            JsfUtil.addErrorMessage("Select Encounter");
            return;
        }
        getPatientEncounterController().getEncounterImage().setImageValue(captureEvent.getData());
        getPatientEncounterController().getEncounterImage().setImageName("encounter_image_" + "000" + ".png");
        getPatientEncounterController().getEncounterImage().setImageType("image/png");
        getPatientEncounterController().getEncounterImage().setEncounter(getPatientEncounterController().getCurrent());
        getPatientEncounterController().getEncounterImage().setClinicalFindingValueType(ClinicalFindingValueType.VisitImage);
        if (getPatientEncounterController().getEncounterImage().getId() == null) {
            clinicalFindingValueFacade.create(getPatientEncounterController().getEncounterImage());
        } else {
            clinicalFindingValueFacade.edit(getPatientEncounterController().getEncounterImage());
        }
        getPatientEncounterController().getEncounterImage().setImageName("encounter_image_" + getPatientEncounterController().getEncounterImage().getId() + ".png");
        clinicalFindingValueFacade.edit(getPatientEncounterController().getEncounterImage());
        getPatientEncounterController().getEncounterImages().add(getPatientEncounterController().getEncounterImage());
        getPatientEncounterController().setEncounterImage(null);
        JsfUtil.addSuccessMessage("Image Added");
    }

    public void oncapture(CaptureEvent captureEvent) {
        String photo = getRandomImageName();
        this.photos.add(0, photo);
        byte[] data = captureEvent.getData();

        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String newFileName = servletContext.getRealPath("") + File.separator + "photocam" + File.separator + photo + ".png";

        FileImageOutputStream imageOutput;
        try {
            imageOutput = new FileImageOutputStream(new File(newFileName));
            imageOutput.write(data, 0, data.length);
            imageOutput.close();
        } catch (IOException e) {
            throw new FacesException("Error in writing captured image.");
        }
    }

    /**
     * Creates a new instance of PhotoCamBean
     */
    public PhotoCamBean() {
    }

    public PatientEncounterController getPatientEncounterController() {
        return patientEncounterController;
    }

}
