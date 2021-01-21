package com.example.currencyconversionservice.controller;

import com.example.currencyconversionservice.CurrencyExchangeServiceProxy;
import com.example.currencyconversionservice.controller.model.CurrencyConversion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CurrencyConversionController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CurrencyExchangeServiceProxy currencyExchangeServiceProxy;

    @GetMapping("/currency-convertor/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convertCurrency(@PathVariable("from") String from, @PathVariable("to") String to, @PathVariable("quantity") Double quantity) {

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000//currency-exchange/from/{from}/to/{to}", CurrencyConversion.class, uriVariables);

        CurrencyConversion responseEntityBody = responseEntity.getBody();

        logger.info("{}", responseEntityBody);

        return new CurrencyConversion(responseEntityBody.getId(), from, to, responseEntityBody.getConversionMultiple(), quantity, quantity*responseEntityBody.getConversionMultiple(), responseEntityBody.getPort());
    }

    @GetMapping("/currency-convertor-feign/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion convertCurrencyFeign(@PathVariable("from") String from, @PathVariable("to") String to, @PathVariable("quantity") Double quantity) {

        CurrencyConversion responseEntityBody = currencyExchangeServiceProxy.retrieveExchangeValue(from, to);

        logger.info("{}", responseEntityBody);

        return new CurrencyConversion(responseEntityBody.getId(), from, to, responseEntityBody.getConversionMultiple(), quantity, quantity*responseEntityBody.getConversionMultiple(), responseEntityBody.getPort());

    }
}
