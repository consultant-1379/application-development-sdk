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
package com.ericsson.enm.sdk.json.parser;


import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
/*
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.io.IOContext;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
*/
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.io.IOContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.ericsson.enm.sdk.json.reader.ActionProperties;
import com.ericsson.enm.sdk.json.reader.CustomConfigJson;
import com.ericsson.enm.sdk.json.reader.URLDetails;
import com.ericsson.enm.sdk.json.reader.CustomApplication;
import com.ericsson.enm.sdk.json.reader.ExecutableDetail;

import com.ericsson.enm.sdk.application.Action;
import com.ericsson.enm.sdk.application.ActionMetadata;
import com.ericsson.enm.sdk.application.ApplicationJson;

public class CustomJsonParser {
    private final static String APPS_FOLDER = "/ericsson/tor/data/apps/";
    private final static String PLUGIN_NAME = "customappintegration/CustomAppPlugin";
    private final static String EXECUTABLE_PLUGIN_NAME = "customappintegration/CustomExecutablePlugin";

    public static <T> T readInputJson(final File jsonFile, final Class<T> destinationClass)
            throws JsonParseException, JsonMappingException, IOException {
        return new ObjectMapper().readValue(jsonFile, destinationClass);
    }

    public boolean validateJson(final CustomConfigJson customConfigJson) {
        boolean valid = true;
        if (null == customConfigJson.getCustomApplications()){
            System.err.println("Invalid Input file: Custom applications are blank...");
            return false;
        }
        final List<CustomApplication> customapps = customConfigJson.getCustomApplications();
        for ( CustomApplication customapp : customapps ){
            if (StringUtils.isBlank(customapp.getAppName())) {
                System.err.println("Invalid Input file: App name is blank...");
                valid = false;
                break;
            }
            if ((customapp.getUrlDetails() == null || customapp.getUrlDetails().isEmpty()) && (customapp.getExecutableDetail() == null || customapp.getExecutableDetail().isEmpty())) {
                System.err.println("Invalid Input file: No URL details or Executable details provided for application "+customapp.getAppName());
                valid = false;
                break;
            }
            if (! (customapp.getUrlDetails() == null || customapp.getUrlDetails().isEmpty())){
                List<URLDetails> urlDetails = customapp.getUrlDetails();
                for (URLDetails urlDetail : urlDetails) {
                    if (StringUtils.isBlank(urlDetail.getGuiLabel())) {
                        System.err.println("Invalid Input file: GuiLable not provided");
                        valid = false;
                        break;
                    }
                    if (StringUtils.isBlank(urlDetail.getProtocol()) || StringUtils.isBlank(urlDetail.getDomainOrIPAddress())) {
                        System.err.println("Invalid Input file: Issue with the protocol OR Domanin not provided");
                        valid = false;
                        break;
                    }
                    if (urlDetail.getActionRule() == null) {
                        System.err.println("Invalid Input file: ActionRule not provided");
                        valid = false;
                        break;
                    }
                    if (urlDetail.getActionRule().getCondition() == null) {
                        System.err.println("Invalid Input file: Condition not provided");
                        valid = false;
                        break;
                    }
                    if (StringUtils.isBlank(urlDetail.getActionRule().getCondition().getDataType())) {
                        System.err.println("Invalid Input file: DataType not provided");
                        valid = false;
                        break;
                    }
                }
            }
            if (! (customapp.getExecutableDetail() == null || customapp.getExecutableDetail().isEmpty())){
                List<ExecutableDetail> executableDetails = customapp.getExecutableDetail();
                for (ExecutableDetail executableDetail : executableDetails) {
                    if (StringUtils.isBlank(executableDetail.getGuiLabel())) {
                        System.err.println("Invalid Input file: GuiLable not provided");
                        valid = false;
                        break;
                    }
                    if (StringUtils.isBlank(executableDetail.getCommandToExecute())) {
                        System.err.println("Invalid Input file: CommandToExecute not provided");
                        valid = false;
                        break;
                    }
                    if (executableDetail.getActionRule() == null) {
                        System.err.println("Invalid Input file: ActionRule not provided");
                        valid = false;
                        break;
                    }
                    if (executableDetail.getActionRule().getCondition() == null) {
                        System.err.println("Invalid Input file: Condition not provided");
                        valid = false;
                        break;
                    }
                    if (StringUtils.isBlank(executableDetail.getActionRule().getCondition().getDataType())) {
                        System.err.println("Invalid Input file: DataType not provided");
                        valid = false;
                        break;
                    }
                }
            }
        }
        return valid;
    }

    public static void writeObject(final Object applicationJson, final File jsonFile) throws IOException {
        final FileWriter jsonFileWriter = new FileWriter(jsonFile);
        final BufferedWriter jsonBufferedWriter = new BufferedWriter(jsonFileWriter);
        final ObjectMapper mapper = new ObjectMapper(new Factory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        final ObjectWriter objectWriter = mapper.writer();
        jsonBufferedWriter.write(objectWriter.writeValueAsString(applicationJson));
        jsonBufferedWriter.close();
    }

    public static void main(final String[] args) {
        final CustomJsonParser parser = new CustomJsonParser();
        if (args.length < 2) {
            System.err.println("Enter one argument");
            return;
        }
        final String tmpAppsFolder = args[0];
        final String inputFile = args[1];
        try {
            final CustomConfigJson customConfigJson = readInputJson(new File(inputFile), CustomConfigJson.class);
            if (!parser.validateJson(customConfigJson)) {
                return;
            }
            if ( null != customConfigJson.getCustomApplications()){
                final List<CustomApplication> customApplications = customConfigJson.getCustomApplications();
                for ( final CustomApplication customApp : customApplications){
                    final String appFolder = APPS_FOLDER + customApp.getAppName();
                    final File appJsonFile = parser.getAppJson(appFolder);
                    final ApplicationJson applicationJson = readInputJson(appJsonFile, ApplicationJson.class);
                    if (customApp.getUrlDetails() != null) {
                        for (final URLDetails urlDetails : customApp.getUrlDetails()) {
                            String actionName = customApp.getAppName() + "-" + urlDetails.getGuiLabel();
                            actionName = actionName.replace(" ", "-");
                            if (null != urlDetails.getActionEnable() && (urlDetails.getActionEnable().equals(true))){
                                parser.handleAppJson(customApp, actionName, applicationJson, urlDetails);
                                parser.updateRules(customApp, actionName, urlDetails, tmpAppsFolder);
                            }
                            if(null != urlDetails.getActionEnable()){
                            parser.validateAction(customApp, actionName, applicationJson, urlDetails.getActionEnable());
                            }
                        }
                    }
                    if (customApp.getExecutableDetail() != null) {
                        for (final ExecutableDetail executableDetail : customApp.getExecutableDetail()) {
                            String actionName = customApp.getAppName() + "-" + executableDetail.getGuiLabel();
                            actionName = actionName.replace(" ", "-");
                            if (null != executableDetail.getActionEnable() && (executableDetail.getActionEnable().equals(true))){
                                parser.handleAppJsonForExecutable(customApp, actionName, applicationJson, executableDetail);
                                parser.updateRulesForExecutable(customApp, actionName, executableDetail, tmpAppsFolder);
                            }
                            if(null != executableDetail.getActionEnable()){
                            parser.validateAction(customApp, actionName, applicationJson, executableDetail.getActionEnable());
                            }
                        }
                    }
                    writeObject(applicationJson, new File(tmpAppsFolder + "" + customApp.getAppName() + "/" + customApp.getAppName() + ".json"));
                }
            }
        } catch (Exception exception) {
            final StringWriter stWriter = new StringWriter();
            final PrintWriter writer = new PrintWriter(stWriter);
            exception.printStackTrace(writer);
            System.err.println(stWriter.toString());
        }
    }

    private void validateAction(CustomApplication customApp, String actionName, ApplicationJson applicationJson, boolean actionEnable) {
        if (!actionEnable){
            // Handling Provided Actions.
            final List<Action> existingProvidedActions = applicationJson.getProvideActions();
            List<Action> disabledActions = new ArrayList<Action>();
            for ( final Action providedAction : existingProvidedActions){
                if (providedAction.getName().equals(actionName)){
                    System.out.println("The action "+actionName+" is removed from the provided actions of application "+customApp.getAppName());
                    disabledActions.add(providedAction);
                }
            }
            applicationJson.getProvideActions().removeAll(disabledActions);

            // Handling consumed Actions.
            final List<String> existingConsumedActions = applicationJson.getConsumeActions();
            List<String> disabledConsumedActions = new ArrayList<String>();
            for (final String eca : existingConsumedActions) {
                if (eca.equalsIgnoreCase(actionName)) {
                    System.out.println("The action "+actionName+" is removed from the consumed actions of application "+customApp.getAppName());
                    disabledConsumedActions.add(eca);
                }
            }
            applicationJson.getConsumeActions().removeAll(disabledConsumedActions);
        }
    }

    private void updateAppJson(final CustomApplication customApp, final String actionName, final ApplicationJson applicationJson,
                               final URLDetails urlDetails) {
        final Action pa = new Action();
        if (null != applicationJson.getConsumeActions()){
            applicationJson.getConsumeActions().add(actionName);
        }
        else{
            final List<String> consumeActions = new ArrayList<String>();
            consumeActions.add(actionName);
            applicationJson.setConsumeActions(consumeActions);
        }
        pa.setName(actionName);
        pa.setPlugin(PLUGIN_NAME);
        if (null != urlDetails.getMultipleSelection()){
            pa.setMultipleSelection(urlDetails.getMultipleSelection());
        }
        else {
            pa.setMultipleSelection(false);
        }
        pa.setPrimary(false);
        if (null != applicationJson.getProvideActions()){
            final List<Action> existingActions = applicationJson.getProvideActions();
            if (StringUtils.isEmpty(urlDetails.getCategory())) {
                for (final Action action : existingActions) {
                    if (action.getName().contains(customApp.getAppName())) {
                        pa.setCategory(action.getCategory());
                        break;
                    }
                }
            } else {
                pa.setCategory(urlDetails.getCategory());
            }
            int maxOrder = 0;
            for (final Action action : existingActions) {
                if (maxOrder < action.getOrder()) {
                    maxOrder = action.getOrder();
                }
            }
            pa.setOrder(maxOrder + 1);
        }
        else {
            pa.setOrder(200);
            if (StringUtils.isEmpty(urlDetails.getCategory())) {
                pa.setCategory("Configuration Management");
            }
            else {
                pa.setCategory(urlDetails.getCategory());
            }
        }
        if (!StringUtils.isEmpty(urlDetails.getProtocol())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("protocol");
            pd.setValue(urlDetails.getProtocol());
            if (pa.getMetadata() == null) {
                final List<ActionMetadata> actionMetaData = new ArrayList<ActionMetadata>();
                actionMetaData.add(pd);
                pa.setMetadata(actionMetaData);
            }
            else {
                pa.getMetadata().add(pd);
            }
        }

        if (!StringUtils.isEmpty(urlDetails.getDomainOrIPAddress())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("domainOrIPAddress");
            pd.setValue(urlDetails.getDomainOrIPAddress());
            pa.getMetadata().add(pd);
        }

        if (!StringUtils.isEmpty(urlDetails.getPort())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("port");
            pd.setValue(urlDetails.getPort());
            pa.getMetadata().add(pd);
        }

        if (!StringUtils.isEmpty(urlDetails.getPath())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("path");
            pd.setValue(urlDetails.getPath());
            pa.getMetadata().add(pd);
        }

        if (!StringUtils.isEmpty(urlDetails.getQueryparams())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("queryparams");
            pd.setValue(urlDetails.getQueryparams());
            pa.getMetadata().add(pd);
        }

        if (!StringUtils.isEmpty(urlDetails.getQueryDelimiter())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("querydelimiter");
            pd.setValue(urlDetails.getQueryDelimiter());
            pa.getMetadata().add(pd);
        }

        if (!StringUtils.isEmpty(urlDetails.getCustomAction())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("customAction");
            pd.setValue(urlDetails.getCustomAction());
            pa.getMetadata().add(pd);
        }

        if (null != applicationJson.getProvideActions()){
            applicationJson.getProvideActions().add(pa);
        }
        else {
            List<Action> providedActions = new ArrayList<Action>();
            providedActions.add(pa);
            applicationJson.setProvideActions(providedActions);
        }

    }

    private void updateAppJsonForExecutable(final CustomApplication customApp, final String actionName, final ApplicationJson applicationJson, final ExecutableDetail executableDetail) {

        if (null != applicationJson.getConsumeActions()) {
            applicationJson.getConsumeActions().add(actionName);
        } else {
            final List<String> consumeActions = new ArrayList<String>();
            consumeActions.add(actionName);
            applicationJson.setConsumeActions(consumeActions);
        }

        final Action pa = new Action();
        pa.setName(actionName);
        pa.setPlugin(EXECUTABLE_PLUGIN_NAME);
        if (null != executableDetail.getMultipleSelection()){
            pa.setMultipleSelection(executableDetail.getMultipleSelection());
        }
        else {
            pa.setMultipleSelection(false);
        }
        pa.setPrimary(false);
        if (null != applicationJson.getProvideActions()){
            final List<Action> existingActions = applicationJson.getProvideActions();
            if (StringUtils.isEmpty(executableDetail.getCategory())) {
                for (final Action action : existingActions) {
                    if (action.getName().contains(customApp.getAppName())) {
                        pa.setCategory(action.getCategory());
                        break;
                    }
                }
            } else {
                pa.setCategory(executableDetail.getCategory());
            }
            int maxOrder = 0;
            for (final Action action : existingActions) {
                if (maxOrder < action.getOrder()) {
                    maxOrder = action.getOrder();
                }
            }
            pa.setOrder(maxOrder + 1);
        }
        else {
            pa.setOrder(200);
            if(StringUtils.isEmpty(executableDetail.getCategory())){
                pa.setCategory("Configuration Management");
            }
            else {
            pa.setCategory(executableDetail.getCategory());
            }
        }
        if (!StringUtils.isEmpty(executableDetail.getCommandToExecute())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("commandToExecute");
            pd.setValue(executableDetail.getCommandToExecute());
            if (pa.getMetadata() == null) {
                final List<ActionMetadata> actionMetaData = new ArrayList<ActionMetadata>();
                actionMetaData.add(pd);
                pa.setMetadata(actionMetaData);
            } else {
                pa.getMetadata().add(pd);
            }
        }
        if (!StringUtils.isEmpty(executableDetail.getContext())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("context");
            pd.setValue(executableDetail.getContext());
            pa.getMetadata().add(pd);
        }

        if (!StringUtils.isEmpty(executableDetail.getCustomAction())) {
            ActionMetadata pd = new ActionMetadata();
            pd.setName("customAction");
            pd.setValue(executableDetail.getCustomAction());
            pa.getMetadata().add(pd);
        }

        if (null != applicationJson.getProvideActions()){
            applicationJson.getProvideActions().add(pa);
        }
        else {
            List<Action> providedActions = new ArrayList<Action>();
            providedActions.add(pa);
            applicationJson.setProvideActions(providedActions);
        }
    }

    private void handleAppJson(final CustomApplication customApp, final String actionName, final ApplicationJson applicationJson,
                               final URLDetails urlDetails) {
        if (null != applicationJson.getConsumeActions()){
            final List<String> existingConsumedActions = applicationJson.getConsumeActions();
            Boolean alreadyexisting = false;
            for (final String eca : existingConsumedActions) {
                if (eca.equalsIgnoreCase(actionName)) {
                    alreadyexisting = true;
                    break;
                }
            }
            if (!alreadyexisting) {
                updateAppJson(customApp, actionName, applicationJson, urlDetails);
            }
        }
        else{
            updateAppJson(customApp, actionName, applicationJson, urlDetails);
        }
    }

    private void handleAppJsonForExecutable(final CustomApplication customApp, final String actionName, final ApplicationJson applicationJson, final ExecutableDetail executableDetail) {
        if (null != applicationJson.getConsumeActions()) {
            final List<String> existingConsumedActions = applicationJson.getConsumeActions();
            Boolean alreadyexisting = false;
            for (final String eca : existingConsumedActions) {
                if (eca.equalsIgnoreCase(actionName)) {
                    alreadyexisting = true;
                    break;
                }
            }
            if (!alreadyexisting) {
                updateAppJsonForExecutable(customApp, actionName, applicationJson, executableDetail);
            }
        } else {
            updateAppJsonForExecutable(customApp, actionName, applicationJson, executableDetail);
        }
    }

    private void updateRules(final CustomApplication customApp, final String actionName, final URLDetails urlDetails, final String tmpAppsFolder)
            throws JsonGenerationException, JsonMappingException, IOException {
        if (null != urlDetails.getActionEnable() &&  (urlDetails.getActionEnable().equals(true))){
            if (urlDetails.getActionRule() != null) {
                urlDetails.getActionRule().setActionName(actionName);
                String fileName = actionName;
                if (null != urlDetails.getActionRule().getCondition().getProperties()) {
                   fileName = fileName + "-" + urlDetails.getActionRule().getCondition().getDataType();
                   final List<ActionProperties> actionproperties = urlDetails.getActionRule().getCondition().getProperties();
                   for (final ActionProperties ap : actionproperties) {
                       if (ap.getValue() != null) {
                            fileName = fileName.toLowerCase() + "-type";
                            fileName = fileName + "-" + ap.getValue();
                       }
                   }
                } else {
                    fileName = fileName + "-any-" + urlDetails.getActionRule().getCondition().getDataType();
                    fileName = fileName.toLowerCase();
                }
                fileName = fileName + ".json";
                final FileWriter fw = new FileWriter(tmpAppsFolder + "" + customApp.getAppName() + "/" + fileName);
                final BufferedWriter bufferedWriter = new BufferedWriter(fw);
                bufferedWriter.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(urlDetails.getActionRule()));
                bufferedWriter.close();
            }
        }
    }

    private void updateRulesForExecutable(final CustomApplication customApp, final String actionName, final ExecutableDetail executableDetail, final String tmpAppsFolder)throws JsonGenerationException, JsonMappingException, IOException {
        if (null != executableDetail.getActionEnable() &&  (executableDetail.getActionEnable().equals(true))){
            if (executableDetail.getActionRule() != null) {
                executableDetail.getActionRule().setActionName(actionName);
                String fileName = actionName;
                if (null != executableDetail.getActionRule().getCondition().getProperties()) {
                   fileName = fileName + "-" + executableDetail.getActionRule().getCondition().getDataType();
                   final List<ActionProperties> actionproperties = executableDetail.getActionRule().getCondition().getProperties();
                   for (final ActionProperties ap : actionproperties) {
                       if (ap.getValue() != null) {
                            fileName = fileName.toLowerCase() + "-type";
                            fileName = fileName + "-" + ap.getValue();
                       }
                   }
                } else {
                    fileName = fileName + "-any-" + executableDetail.getActionRule().getCondition().getDataType();
                    fileName = fileName.toLowerCase();
                }
                fileName = fileName + ".json";
                final FileWriter fw = new FileWriter(tmpAppsFolder + "" + customApp.getAppName() + "/" + fileName);
                final BufferedWriter bufferedWriter = new BufferedWriter(fw);
                bufferedWriter.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(executableDetail.getActionRule()));
                bufferedWriter.close();
            }
        }
    }

    private File getAppJson(final String appFolder) {
        File jsonFile = null;
        for (final File file : new File(appFolder).listFiles()) {
            if (file.getName().endsWith(".json")) {
                jsonFile = file;
                break;
            }
        }
        return jsonFile;
    }

    private static class AppJsonPrettyPrinter extends DefaultPrettyPrinter {

        public static final AppJsonPrettyPrinter instance = new AppJsonPrettyPrinter();

        public AppJsonPrettyPrinter() {
            _arrayIndenter = _objectIndenter;
        }
    }
    
    private static class Factory extends JsonFactory {
        @Override
        protected JsonGenerator _createGenerator(final Writer out, final IOContext ctxt) throws IOException {
            return super._createGenerator(out, ctxt).setPrettyPrinter(AppJsonPrettyPrinter.instance);
        }
    }
}
