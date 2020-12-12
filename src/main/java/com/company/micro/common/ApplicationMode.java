package com.company.micro.common;

import com.company.micro.helper.MockHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Application mode class to control the modes.
 */
@Component
public class ApplicationMode {
    /**
     * Test mode or not.
     */
    @Value("${application.mode.test}")
    private boolean testMode;

    /**
     * data update mode.
     */
    @Value("${application.mode.data-update}")
    private boolean dataUpdateMode;

    /**
     * Mocker class.
     */
    private MockHelper mocker;

    @Autowired
    public ApplicationMode(final MockHelper mockHelper) {
        this.mocker = mockHelper;
    }

    /**
     * Application is in test mode or not.
     * Api will not hit the actual implementation in this mode.
     *
     * @return boolean test mode or not.
     */
    public boolean isTestMode() {
        return testMode;
    }

    /**
     * Application is in data update mode or not.
     * Actual data will not be inserted in this mode
     *
     * @return boolean data update mode or not
     */
    public Boolean isDataUpdateMode() {
        return dataUpdateMode;
    }

    /**
     * Get the mocker object.
     *
     * @return MockHelper mocker object
     */
    public MockHelper mocker() {
        return mocker;
    }
}

