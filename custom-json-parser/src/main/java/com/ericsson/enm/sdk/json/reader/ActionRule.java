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
import org.codehaus.jackson.map.annotate.JsonSerialize;
*/
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class ActionRule {

    private String actionName;

    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private ActionCondition condition;

    /**
     * @return the condition
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    public ActionCondition getCondition() {
        return condition;
    }

    /**
     * @param condition
     *            the condition to set
     */
    public void setCondition(ActionCondition condition) {
        this.condition = condition;
    }

    /**
     * @return the actionName
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * @param actionName
     *            the actionName to set
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public String toString() {
        return "ActionRule{" + "actionName='" + actionName + '\'' + ", condition=" + condition + '}';
    }
}
