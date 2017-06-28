package com.example.textSearchEngine;

import com.example.textSearchEngine.controller.SearchApiController;
import com.example.textSearchEngine.dto.response.SearchResponse;
import com.example.textSearchEngine.dto.response.codes.ResponseCodes;
import com.example.textSearchEngine.service.IRequestValidationService;
import com.example.textSearchEngine.service.ISearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suryansh on 23/6/17.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(value = SearchApiController.class, secure = false)
public class SearchApiUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISearchService searchService;

    @MockBean
    IRequestValidationService requestValidationService;

    @Test
    public void emptySearchQuery() throws Exception {
        SearchResponse emptySearchQueryResponse = new SearchResponse(null, null, true, ResponseCodes.SUCCESSFUL.getCode(), "Token not found");
        String emptySearchQueryResponseJson = "{\"validationError\":{\"code\":200,\"message\":\"Bad Request\"},\"documentToListOfLinesMap\":null,\"successful\":false,\"code\":null,\"message\":null}";
        Mockito.when(searchService.searchToken(Mockito.anyString())).thenReturn(emptySearchQueryResponse);
        Mockito.when(requestValidationService.validateInputRequest(Mockito.any())).thenReturn(null);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/findwordinfile").param("word", "").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());

        JSONAssert.assertEquals(emptySearchQueryResponseJson, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void tokenFoundSuccessfully() throws Exception {
        SearchResponse tokenFoundResponse = new SearchResponse(null, null, true, ResponseCodes.SUCCESSFUL.getCode(), "Token found");

        Map<String, List<String>> documentNameToLineMap = new HashMap<>();
        List<String> lines = new ArrayList<String>();
        lines.add("2015-06-20 07:28:09:INFO:com.snapdeal.common.LOG - dblib::Executing Procedure::GET_CART");
        lines.add("2015-06-20 07:28:09:DEBUG:org.springframework.jdbc.core.simple.SimpleJdbcCall - The following parameters are used for call {call PKG_SHOPPING_CART.GET_CART(?, ?, ?, ?, ?)} with: {pi_cart_id=8030}");

        documentNameToLineMap.put("myapp-1.3.6.log", lines);
        tokenFoundResponse.setDocumentToListOfLinesMap(documentNameToLineMap);
        String tokenFoundResponseJson = "{\"validationError\":null,\"documentToListOfLinesMap\":{\"myapp-1.3.6.log\":[\"2015-06-20 07:28:09:INFO:com.snapdeal.common.LOG - dblib::Executing Procedure::GET_CART\",\"2015-06-20 07:28:09:DEBUG:org.springframework.jdbc.core.simple.SimpleJdbcCall - The following parameters are used for call {call PKG_SHOPPING_CART.GET_CART(?, ?, ?, ?, ?)} with: {pi_cart_id=8030}\"]},\"successful\":true,\"code\":200,\"message\":\"Token found\"}";
        Mockito.when(searchService.searchToken(Mockito.anyString())).thenReturn(tokenFoundResponse);
        Mockito.when(requestValidationService.validateInputRequest(Mockito.any())).thenReturn(null);


        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/findwordinfile").param("word", "GET_CART").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());

        JSONAssert.assertEquals(tokenFoundResponseJson, result.getResponse()
                .getContentAsString(), false);
    }

}