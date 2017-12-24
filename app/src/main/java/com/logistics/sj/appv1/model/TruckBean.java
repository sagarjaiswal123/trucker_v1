package com.logistics.sj.appv1.model;

import com.logistics.sj.appv1.R;

import java.io.Serializable;

/**
 * Created by AJ on 03-05-2017.
 */

public class TruckBean implements Serializable {

    private String truckNumber;
    private String truckOwnerName;
    private String[] truckOwnerPhoneNumberS;
    private String[] accountNumbers;
    private int icon = android.R.drawable.ic_menu_add;

    public String getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(String truckNumber) {
        this.truckNumber = truckNumber;
    }

    public String getTruckOwnerName() {
        return truckOwnerName;
    }

    public void setTruckOwnerName(String truckOwnerName) {
        this.truckOwnerName = truckOwnerName;
    }

    public String[] getTruckOwnerPhoneNumberS() {
        return truckOwnerPhoneNumberS;
    }

    public void setTruckOwnerPhoneNumberS(String[] truckOwnerPhoneNumberS) {
        this.truckOwnerPhoneNumberS = truckOwnerPhoneNumberS;
    }

    public String[] getAccountNumbers() {
        return accountNumbers;
    }

    public void setAccountNumbers(String[] accountNumbers) {
        this.accountNumbers = accountNumbers;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}