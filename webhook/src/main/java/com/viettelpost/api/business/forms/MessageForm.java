package com.viettelpost.api.business.forms;

import com.viettelpost.api.business.models.EntryData;

import java.io.Serializable;
import java.util.List;

public class MessageForm implements Serializable {
    String object;
    List<EntryData> entry;

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public List<EntryData> getEntry() {
        return entry;
    }

    public void setEntry(List<EntryData> entry) {
        this.entry = entry;
    }
}
