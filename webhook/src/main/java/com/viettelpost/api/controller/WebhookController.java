package com.viettelpost.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.viettelpost.api.base.*;
import com.viettelpost.api.business.CustomerService;
import com.viettelpost.api.business.forms.MessageForm;
import com.viettelpost.api.responses.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class WebhookController extends BaseController {
    Logger logger = Logger.getLogger(WebhookController.class);

    ObjectMapper mapper = new ObjectMapper();
    ClientConfig config = new DefaultClientConfig();
    Client client = Client.create(config);

    public WebhookController() {
        client.setReadTimeout(30000);
        client.setConnectTimeout(30000);
    }

    @Autowired
    CustomerService customerService;

    @Override
    public Logger getLogger() {
        return logger;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Resource> test() throws Exception{
        Resource resource = new UrlResource("classpath:thumpnail.jpg");
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }

    @RequestMapping(value = "/webhook", method = RequestMethod.GET)
    public ResponseEntity testWebHook(@RequestParam("hub.mode") String hubMode, @RequestParam("hub.verify_token") String hubVerify, @RequestParam("hub.challenge") String hubChallenge ) {
        if ("subscribe".equals(hubMode) && Constants.verify_token.equals(hubVerify)){
            System.out.println("WEBHOOK_VERIFIED");
            return new ResponseEntity(hubChallenge,HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @RequestMapping(value = "/webhook", method = RequestMethod.POST)
    public ResponseEntity sendMessage(@RequestBody MessageForm form) {
        try {
            if ("page".equals(form.getObject())){
                String senderId = form.getEntry().get(0).getSenderId();
                System.out.println(senderId);
                String message = form.getEntry().get(0).getMessage();
                MessageResponse response = new MessageResponse("RESPONSE",senderId, customerService.getReply(message));
                String data = client.resource(Constants.apiURL)
                        .queryParam("access_token", Constants.access_token)
                        .type("application/json")
                        .post(String.class, mapper.writeValueAsString(response));
                return new ResponseEntity("OK",HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
