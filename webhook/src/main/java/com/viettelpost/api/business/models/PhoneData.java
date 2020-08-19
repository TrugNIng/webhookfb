package com.viettelpost.api.business.models;

import java.io.Serializable;

public class PhoneData implements Serializable {
    String oldPhone;
    String newPhone;

    public PhoneData(String oldPhone, String newPhone) {
        this.oldPhone = oldPhone;
        this.newPhone = newPhone;
    }

    public String getOldPhone() {
        return oldPhone;
    }

    public void setOldPhone(String oldPhone) {
        this.oldPhone = oldPhone;
    }

    public String getNewPhone() {
        return newPhone;
    }

    public void setNewPhone(String newPhone) {
        this.newPhone = newPhone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof String) {
            return oldPhone.equals((String) obj) || newPhone.equals((String) obj);
        }
        if (obj instanceof PhoneData) {
            PhoneData phone = (PhoneData) obj;
            return (phone.getOldPhone() != null && oldPhone.equals(phone.getOldPhone())) || (phone.getNewPhone() != null && newPhone.equals(phone.getNewPhone()));
        }
        return super.equals(obj);
    }
}
