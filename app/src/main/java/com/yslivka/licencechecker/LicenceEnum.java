package com.yslivka.licencechecker;

public enum LicenceEnum{
    TRIAL("Trial version"), PRO("Pro version"), ERROR("Error, please try again");

    private String mLicence;

    private LicenceEnum(String licence) {
        mLicence = licence;
    }

    public String getLicence() {
        return mLicence;
    }
}
