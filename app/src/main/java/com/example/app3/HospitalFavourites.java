package com.example.app3;

import org.litepal.crud.LitePalSupport;

public class HospitalFavourites extends LitePalSupport {

    String ownerTel;
    String hospitalName;
    String hospitalTel;
    String hospitalAddress;
    String hospitalDistance;
    String hospitalTime;

    public String getOwnerTel() {
        return ownerTel;
    }

    public void setOwnerTel(String ownerTel) {
        this.ownerTel = ownerTel;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalTel() {
        return hospitalTel;
    }

    public void setHospitalTel(String hospitalTel) {
        this.hospitalTel = hospitalTel;
    }

    public String getHospitalAddress() {
        return hospitalAddress;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public String getHospitalDistance() {
        return hospitalDistance;
    }

    public void setHospitalDistance(String hospitalDistance) {
        this.hospitalDistance = hospitalDistance;
    }

    public String getHospitalTime() {
        return hospitalTime;
    }

    public void setHospitalTime(String hospitalTime) {
        this.hospitalTime = hospitalTime;
    }
}
