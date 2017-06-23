/*
package com.example.textSearchEngine;

import com.example.textSearchEngine.controller.SearchApiController;
import com.example.textSearchEngine.service.ISearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

*/
/**
 * Created by suryansh on 23/6/17.
 *//*

@RunWith(SpringRunner.class)
@WebMvcTest(value = SearchApiController.class, secure = false)
public class SearchApiUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISearchService searchService;

    Course mockCourse = new Course("Course1", "Spring", "10 Steps",
            Arrays.asList("Learn Maven", "Import Project", "First Example",
                    "Second Example"));

    String exampleCourseJson = "{\"name\":\"Spring\",\"description\":\"10 Steps\",\"steps\":[\"Learn Maven\",\"Import Project\",\"First Example\",\"Second Example\"]}";

    @Test
    public void retrieveDetailsForCourse() throws Exception {

        Mockito.when(
                searchService.retrieveCourse(Mockito.anyString(),
                        Mockito.anyString())).thenReturn(mockCourse);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/students/Student1/courses/Course1").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());
        String expected = "{id:Course1,name:Spring,description:10 Steps}";

        // {"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

}
*/
