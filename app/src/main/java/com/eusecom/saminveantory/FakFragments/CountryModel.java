package com.eusecom.saminveantory.FakFragments;

/**
 * Created by iFocus on 29-10-2015.
 */
public class CountryModel {

    String name;
    String isocode;
    String fakhod;

    CountryModel(String name, String isocode, String fakhod){
        this.name=name;
        this.isocode=isocode;
        this.fakhod=fakhod;
    }

    public String getName() {
        return name;
    }

    public String getisoCode() {
        return isocode;
    }

    public String getfakhod() {
        return fakhod;
    }
}
