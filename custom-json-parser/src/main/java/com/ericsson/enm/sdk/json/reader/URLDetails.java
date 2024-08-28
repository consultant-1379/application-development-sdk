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
public class URLDetails {
    private String guiLabel;
    private String pluginName;
    private String category;
    private ActionRule actionRule;
    private String protocol;
    private String domainOrIPAddress;
    private String queryDelimiter;
    private String port;
    private String path;
    private String queryparams;
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
     * @return the actionEnable
     */
    public Boolean getActionEnable() {
        return actionEnable;
    }

    /**
     * @param actionEnable the actionEnable to set
     */
    public void setActionEnable(Boolean actionEnable) {
        this.actionEnable = actionEnable;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the protocol
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * @param protocol the protocol to set
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * @return the domainOrIPAddress
     */
    public String getDomainOrIPAddress() {
        return domainOrIPAddress;
    }

    /**
     * @param domainOrIPAddress the domainOrIPAddress to set
     */
    public void setDomainOrIPAddress(String domainOrIPAddress) {
        this.domainOrIPAddress = domainOrIPAddress;
    }

    /**
     * @return the queryDelimiter
     */
    public String getQueryDelimiter() {
        return queryDelimiter;
    }

    /**
     * @param queryDelimiter the queryDelimiter to set
     */
    public void setQueryDelimiter(String queryDelimiter) {
        this.queryDelimiter = queryDelimiter;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return the queryparam
     */
    public String getQueryparams() {
        return queryparams;
    }

    /**
     * @param queryparams the queryparam to set
     */
    public void setQueryparams(String queryparams) {
        this.queryparams = queryparams;
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
        builder.append("protocol = " + protocol);
        builder.append("domainOrIPAddress = " + domainOrIPAddress);
        builder.append("queryDelimiter = " + queryDelimiter);
        builder.append("path = " + path);
        builder.append("port = " + port);
        builder.append("queryparam = " + queryparams);
        builder.append(";category = " + category);
        builder.append(";actionRule = " + actionRule);
        builder.append(";customAction = " + customAction);
        return builder.toString();
    }

}
