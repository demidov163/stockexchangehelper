package com.demidov.projects.stockexhangehelper.service.impl;

import com.demidov.projects.stockexhangehelper.data.StockShareParameters;
import com.demidov.projects.stockexhangehelper.service.StockExchangeRequestService;
import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class StockExchangeRequestServiceImpl implements StockExchangeRequestService {

    @Value(value = "${stockexhange.host}")
    private String host;


    @Override
    public List<String> getStockShareHistoryInfo(StockShareParameters parameters) {
        RestTemplate restTemplate = new RestTemplate();

        StringBuilder url = new StringBuilder().append(host)
                .append("/iss/history/engines/stock/markets/shares/securities/")
                .append(parameters.getShareName())
                .append("?");
        prepareQueryParameters(parameters, url);

        String shareInfoResponse = restTemplate.getForObject(url.toString(), String.class);

        String xPathToRows = "/document/data[@id='history']/rows/row";

        XMLDocument xmlDocument = new XMLDocument(shareInfoResponse);
        List<XML> nodes = xmlDocument.nodes(xPathToRows);

        return nodes.stream()
                .flatMap(node -> node.xpath("@" + parameters.getAttributeIndex()).stream())
                .collect(Collectors.toList());
    }

    private void prepareQueryParameters(StockShareParameters parameters, StringBuilder url) {
        Map<String, String> queryParameters = new HashMap<>();
        if (StringUtils.isNotEmpty(parameters.getFromDate())) {
            queryParameters.put("from", parameters.getFromDate());
        }
        if (StringUtils.isNotEmpty(parameters.getTillDate())) {
            queryParameters.put("till", parameters.getTillDate());
        }

        if (!queryParameters.isEmpty()) {
            url.append("?");
            for (Map.Entry<String, String> param : queryParameters.entrySet()) {
                url.append(param.getKey()).append("=").append(param.getValue()).append("&");
            }
        }
    }
}
