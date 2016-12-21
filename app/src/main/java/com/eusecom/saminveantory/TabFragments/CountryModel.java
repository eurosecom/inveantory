package com.eusecom.saminveantory.TabFragments;

/**
 * Created by iFocus on 29-10-2015.
 */
public class CountryModel {

    String name;
    String isocode;
    String iscen;
    String ismer;
    String iscis;

    CountryModel(String name, String isocode){
        this.name=name;
        this.isocode=isocode;
    }

    CountryModel(String name, String isocode, String iscen, String ismer, String iscis){
        this.name=name;
        this.isocode=isocode;
        this.iscen=iscen;
        this.ismer=ismer;
        this.iscis=iscis;
    }

    public String getName() {
        return name;
    }

    public String getisoCode() {
        return isocode;
    }

    public String getisCen() {
        return iscen;
    }

    public String getisMer() {
        return ismer;
    }

    public String getisCis() {
        return iscis;
    }
}
