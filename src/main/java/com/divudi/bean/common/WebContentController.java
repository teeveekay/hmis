/*
 * Open Hospital Management Information System
 *
 * Dr M H B Ariyaratne
 * Acting Consultant (Health Informatics)
 * (94) 71 5812399
 * (94) 71 5812399
 */
package com.divudi.bean.common;

import com.divudi.entity.WebContent;
import com.divudi.facade.WebContentFacade;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Dr. M. H. B. Ariyaratne, MBBS, MSc, MD(Health Informatics)
 * Acting Consultant (Health Informatics)
 */
@Named
@SessionScoped
public class WebContentController implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    SessionController sessionController;
    @EJB
    private WebContentFacade ejbFacade;
    private WebContent selected;
    private List<WebContent> items = null;

    
    public String toAddNewWebContent(){
        return "/webcontent/webcontent";
    }
    
    public String toListWebContent(){
        return "/webcontent/webcontent";
    }
    
    public List<WebContent> completeWebContent(String qry) {
        List<WebContent> list;
        String sql;
        HashMap hm = new HashMap();
        sql = "select c from WebContent c "
                + " where c.retired=false "
                + " and upper(c.name) like :q "
                + " order by c.name";
        hm.put("q", "%" + qry.toUpperCase() + "%");
        list = getFacade().findBySQL(sql, hm);

        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public void prepwebContentdd() {
        selected = new WebContent();
    }

    private void recreateModel() {
        items = null;
    }

    public void saveSelected() {
        if (getSelected().getId() != null && getSelected().getId() > 0) {
            getFacade().edit(selected);
            UtilityController.addSuccessMessage("Updated Successfully.");
        } else {
            getFacade().create(selected);
            UtilityController.addSuccessMessage("Saved Successfully");
        }
        recreateModel();
        getItems();
    }

    public WebContentFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(WebContentFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public SessionController getSessionController() {
        return sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public WebContentController() {
    }

    public WebContent getSelected() {
        if (selected == null) {
            selected = new WebContent();
        }
        return selected;
    }

    public void setSelected(WebContent selected) {
        this.selected = selected;
    }

    public void delete() {
        if (selected != null) {
            selected.setRetired(true);
            getFacade().edit(selected);
            UtilityController.addSuccessMessage("Deleted Successfully");
        } else {
            UtilityController.addSuccessMessage("Nothing to Delete");
        }
        recreateModel();
        getItems();
        selected = null;
        getSelected();
    }

    private WebContentFacade getFacade() {
        return ejbFacade;
    }

    public void listItems() {
        String j;
        j = "select a "
                + " from WebContent a "
                + " where a.retired=false "
                + " order by a.name";
        items = getFacade().findBySQL(j);
    }

    public List<WebContent> getItems() {
        return items;
    }

    /**
     *
     */
    @FacesConverter(forClass = WebContent.class)
    public static class WebContentConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WebContentController controller = (WebContentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "webContentController");
            return controller.getEjbFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof WebContent) {
                WebContent o = (WebContent) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + WebContentController.class.getName());
            }
        }
    }

    @FacesConverter("webContentCon")
    public static class WebContentControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WebContentController controller = (WebContentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "webContentController");
            return controller.getEjbFacade().find(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof WebContent) {
                WebContent o = (WebContent) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type "
                        + object.getClass().getName() + "; expected type: " + WebContentController.class.getName());
            }
        }
    }
}