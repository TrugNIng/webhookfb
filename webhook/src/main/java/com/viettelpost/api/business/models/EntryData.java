package com.viettelpost.api.business.models;

import java.io.Serializable;
import java.util.List;

public class EntryData implements Serializable {
    String id;
    Long time;
    List<Messaging> messaging;

    public String getSenderId(){
        return messaging.get(0).getSender().getId();
    }
    public String getMessage(){
        return messaging.get(0).getMessage().getText();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<Messaging> getMessaging() {
        return messaging;
    }

    public void setMessaging(List<Messaging> messaging) {
        this.messaging = messaging;
    }

    static class Messaging{
        Sender sender;
        Recipient recipient;
        Long timestamp;
        Message message;

        public Sender getSender() {
            return sender;
        }

        public void setSender(Sender sender) {
            this.sender = sender;
        }

        public Recipient getRecipient() {
            return recipient;
        }

        public void setRecipient(Recipient recipient) {
            this.recipient = recipient;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }

        public Message getMessage() {
            return message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }
    }

    static  class Sender{
        String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static  class Recipient{
        String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static  class Message{
        String mid;
        QuickReply quick_reply;
        String text;

        public String getText() {
            return text;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public QuickReply getQuick_reply() {
            return quick_reply;
        }

        public void setQuick_reply(QuickReply quick_reply) {
            this.quick_reply = quick_reply;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    static  class QuickReply{
        String payload;

        public String getPayload() {
            return payload;
        }

        public void setPayload(String payload) {
            this.payload = payload;
        }
    }

}
