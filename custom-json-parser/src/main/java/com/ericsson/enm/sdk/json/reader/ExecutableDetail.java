/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2018
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.enm.sdk.json.reader;
/*
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
*/
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExecutableDetail {
    private String guiLabel;
    private String pluginName;
    private String category;
    private String commandToExecute;
    private ActionRule actionRule;
    private String context;
    private Boolean actionEnable;
    private String customAction;
    private Boolean multipleSelection;

    /**
     * @return the multipleSelection
     */
    public Boolean getMultipleSelection() {
        return multipleSelection;
    }

     /**
     * @param multipleSelection the multipleSelection to set
     */
    public void setMultipleSelection(Boolean multipleSelection) {
        this.multipleSelection = multipleSelection;
    }


    /**
     * @return the customAction
     */
    public String getCustomAction() {
        return customAction;
    }

    /**
     * @param customAction the customAction to set
     */
    public void setCustomAction(String customAction) {
        this.customAction = customAction;
    }

    /**
     * @return the actionEnabled
     */
    public Boolean getActionEnable() {
        return actionEnable;
    }

    /**
     * @param actionEnable the actionEnabled to set
     */
    public void setActionEnable(Boolean actionEnable) {
        this.actionEnable = actionEnable;
    }



    /**
     * @return the queryparam
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context the queryparam to set
     */
    public void setContext(String context) {
        this.context = context;
    }



    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category
     *            the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the guiLabel
     */
    public String getGuiLabel() {
        return guiLabel;
    }

    /**
     * @param guiLabel
     *            the guiLabel to set
     */
    public void setGuiLabel(String guiLabel) {
        this.guiLabel = guiLabel;
    }

    /**
     * @return the pluginName
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * @param pluginName
     *            the pluginName to set
     */
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    /**
     * @return the actionRule
     */
    public ActionRule getActionRule() {
        return actionRule;
    }

    /**
     * @param actionRule
     *            the actionRule to set
     */
    public void setActionRule(ActionRule actionRule) {
        this.actionRule = actionRule;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("guiLabel = " + guiLabel);
        builder.append("commandToExecute = " + commandToExecute);
        builder.append("context = " + context);
        builder.append(";category = " + category);
        builder.append(";actionEnable = " + actionEnable);
        builder.append(";actionRule = " + actionRule);
        builder.append(";customAction = " + customAction);

        return builder.toString();
    }

    public String getCommandToExecute() {
        return commandToExecute;
    }

    public void setCommandToExecute(String cmdToExecute) {
        this.commandToExecute = cmdToExecute;
    }

}
