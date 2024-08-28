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

import java.util.List;

public class CustomApplication {

    private String appName;
    private List<URLDetails> urlDetails;
    private List<ExecutableDetail> executableDetail;

    /**
     * @return the appName
    */
    public String getAppName() {
        return appName;
    }

    /**
     * @param appName
      *            the appName to set
     */
    public void setAppName(String appName) {
       this.appName = appName;
    }

    /**
     * @return the urlDetails
     */
    public List<URLDetails> getUrlDetails() {
        return urlDetails;
    }

    /**
     * @param urlDetails
     *            the urlDetails to set
     */
    public void setUrlDetails(List<URLDetails> urlDetails) {
        this.urlDetails = urlDetails;
    }

    /**
      * @return the executableDetail
    */
    public List<ExecutableDetail> getExecutableDetail() {
       return executableDetail;
    }

    /**
     * @param executableDetail
     *            the executableDetail to set
     */
    public void setExecutableDetail(List<ExecutableDetail> executableDetail) {
        this.executableDetail = executableDetail;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
    */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("appName = " + appName);
        builder.append(";urlDetails = " + urlDetails);
        builder.append(";executableDetail = " + executableDetail);
        return builder.toString();
    }

}
