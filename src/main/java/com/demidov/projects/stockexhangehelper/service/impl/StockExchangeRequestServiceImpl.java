package com.demidov.projects.stockexhangehelper.service.impl;

import com.demidov.projects.stockexhangehelper.data.StockShareParameters;
import com.demidov.projects.stockexhangehelper.data.statistic.StatisticData;
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

    private static final String BOARD_ID_TQBR = "TQBR";


    // <row id="129" board_group_id="57" boardid="TQBR" title="Т+: Акции и ДР - безадрес." is_traded="1" />
    @Override
    public List<StatisticData> getStockShareHistoryInfo(StockShareParameters parameters) {
        RestTemplate restTemplate = new RestTemplate();

        StringBuilder url = new StringBuilder().append(host)
                .append("/iss/history/engines/stock/markets/shares/securities/")
                .append(parameters.getShareName())
                .append("?");
        prepareQueryParameters(parameters, url);

        String shareInfoResponse = restTemplate.getForObject(url.toString(), String.class);

        String xPathToRows = "/document/data[@id='history']/rows/row[@BOARDID='TQBR']";

        XMLDocument xmlDocument = new XMLDocument(shareInfoResponse);
        List<XML> nodes = xmlDocument.nodes(xPathToRows);

        return nodes.stream()
                .map(node -> mapStatisticDate(node, parameters.getAttributeIndex()))
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

    private StatisticData mapStatisticDate(XML node, String attributePriceName) {
        final List<String> xpathDate = node.xpath("@TRADEDATE");
        final List<String> xpathPrice = node.xpath("@" + attributePriceName);
        String price = xpathPrice.iterator().next();
        return StatisticData.builder()
                .date(xpathDate.isEmpty() ? "" : xpathDate.iterator().next())
                .price(StringUtils.isNotEmpty(price) ? Double.parseDouble(price) :  0).build();
    }
}
