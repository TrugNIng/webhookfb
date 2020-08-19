package com.viettelpost.api.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by Oto on 16/10/2018.
 */
public class MessageResponse implements Serializable {
    String messaging_type;
    Recipient recipient;
    Message message;

    public MessageResponse(String messaging_type, String senderId, String message) {
        this.messaging_type = messaging_type;
        this.recipient = new Recipient(senderId);
        this.message = new Message(message);
    }

    public String getMessaging_type() {
        return messaging_type;
    }

    public void setMessaging_type(String messaging_type) {
        this.messaging_type = messaging_type;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    static class Recipient{
        String id;

        public Recipient(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    static class Message{
        String text;

        public Message(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
