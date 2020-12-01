package edu.eci.arsw.teachtome.testcontroller.tests;

import edu.eci.arsw.teachtome.controllers.dtos.GetUserDTO;
import edu.eci.arsw.teachtome.testcontroller.BasicControllerTestsUtilities;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertNotEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class VideoTokenTest extends BasicControllerTestsUtilities {

    @Before
    public void setUpTest() throws Exception {
        super.setUpUser();
    }

    @Test
    public void shouldGetTheVideoToken() throws Exception {
        String email = "video1@gmail.com";
        addUser(email);
        GetUserDTO user = getUser(email);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/" + user.getId() + "/token").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertNotEquals("", bodyResult);
    }
}
