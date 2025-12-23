package com.example.demo.infra.webservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;

@Configuration
public class SoapClientConfig {

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller m = new Jaxb2Marshaller();
        // TODO: generated JAXB classes package
        // m.setContextPath("com.example.demo.generated");
        return m;
    }

    @Bean
    public WebServiceTemplate webServiceTemplate(Jaxb2Marshaller marshaller) {
        WebServiceTemplate t = new WebServiceTemplate();
        t.setMarshaller(marshaller);
        t.setUnmarshaller(marshaller);
        return t;
    }
}
