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
package com.ericsson.enm.sdk.application;

import java.io.Serializable;
/*
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
*/
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ActionMetadata implements Serializable{

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(final String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "ActionMetadata{" +
                " name='" + name +
                " value='" + value+
                '}';
    }
}
