package com.viettelpost.api.base;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.Ssl;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class WebHookContainer implements EmbeddedServletContainerCustomizer {

    @Override
    public void customize(ConfigurableEmbeddedServletContainer cfg) {
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        tomcat.addAdditionalTomcatConnectors(getHttpConnector());
        return tomcat;
    }

    private Connector getHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(80);
        connector.setSecure(false);
        connector.setRedirectPort(443);
        return connector;
    }

//    private Ssl ssl() {
//        Ssl ssl = new Ssl();
//        ssl.setKeyAlias("tomcat");
//        ssl.setEnabledProtocols(new String[]{"TLSv1", "TLSv1.1", "TLSv1.2"});
//        ssl.setCiphers(new String[]{"TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256", "TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384", "TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA", "TLS_ECDHE_RSA_WITH_RC4_128_SHA", "TLS_RSA_WITH_AES_128_CBC_SHA256", "TLS_RSA_WITH_AES_128_CBC_SHA", "TLS_RSA_WITH_AES_256_CBC_SHA256", "TLS_RSA_WITH_AES_256_CBC_SHA", "SSL_RSA_WITH_RC4_128_SHA"});
//        ssl.setKeyStorePassword("changit");
//        ssl.setKeyStore("/home/luannt/app/cfg/ssl/server.jks");
//        return ssl;
//    }
//
//    @Bean
//    public EmbeddedServletContainerFactory servletContainerFactory() {
//        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
//        factory.setProtocol("org.apache.coyote.http11.Http11NioProtocol");
//        factory.setDisplayName("webhook");
//        factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
//            @Override
//            public void customize(Connector connector) {
//                connector.setMaxPostSize(5242880);
//                connector.setPort(80);
//                Http11NioProtocol ab = (Http11NioProtocol) connector.getProtocolHandler();
//                ab.setAcceptCount(1000);
//                ab.setMaxConnections(-1);
//                ab.setMaxHeaderCount(8192);
//                ab.setSecure(true);
//                ab.setCompression("on");
//                ab.setMaxThreads(10000);
//                ab.setMinSpareThreads(25);
//                ab.setConnectionTimeout(15000);
//                ab.setCompressibleMimeType("text/html,text/xml,text/javascript,text/css,text/plain,application/json");
//            }
//        });
//        return factory;
//    }
}
