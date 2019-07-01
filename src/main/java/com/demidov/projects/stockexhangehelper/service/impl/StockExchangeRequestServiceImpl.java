package com.demidov.projects.stockexhangehelper.service.impl;

import com.demidov.projects.stockexhangehelper.data.StockShareParameters;
import com.demidov.projects.stockexhangehelper.service.StockExchangeRequestService;
import com.jcabi.immutable.Array;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class StockExchangeRequestServiceImpl implements StockExchangeRequestService {

    @Value(value = "${stockexhange.host}")
    private String host;


    @Override
    public List<String> getStockShareHistoryInfo(StockShareParameters parameters) {
        RestTemplate restTemplate = new RestTemplate();
        String url = new StringBuilder().append(host)
                .append("/iss/history/engines/stock/markets/shares/securities/")
                .append(parameters.getShareName())
                .append(".xml").toString();
        String shareInfoResponse = restTemplate.getForObject(url, String.class);

        String xPathToRows = "/document/data[@id='history']/rows/row";

        XMLDocument xmlDocument = new XMLDocument(shareInfoResponse);
        List<XML> nodes = xmlDocument.nodes(xPathToRows);

        return nodes.stream().flatMap(node -> node.xpath("@" + parameters.getAttributeIndex()).stream()).collect(Collectors.toList());
    }
}
