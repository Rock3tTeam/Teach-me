package edu.eci.arsw.teachtome.testcontroller;

import com.google.gson.Gson;
import edu.eci.arsw.teachtome.ClassUtilities;
import edu.eci.arsw.teachtome.auth.UserDetailsImpl;
import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BasicControllerUtilities implements ClassUtilities {

    @Autowired
    protected MockMvc mvc;

    protected final Gson gson = new Gson();
    protected final String patternDate = "yyyy-MM-dd";
    protected final String patternHour = "HH:mm:ss";
    protected final String apiRoot = "/api/v1";
    protected String token;

    public void setUpUser() throws Exception {
        User user = addUser("authenticated@hotmail.com");
        UserDetailsImpl userDetails = new UserDetailsImpl(user.getEmail(), user.getPassword());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userDetails)))
                .andExpect(status().isOk())
                .andReturn();
        token = result.getResponse().getHeader("Authorization");
    }

    @Override
    public User addUser(String email) throws Exception {
        User user = new User(email, "Juan", "Rodriguez", "nuevo", email);
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        return user;
    }

    @Override
    public Clase addClassAndTeacher(String teacherEmail, String className, String classDescription) throws Exception {
        addUser(teacherEmail);
        Clase clase = getClase(className, classDescription);
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes").header("Authorization", token).header("x-userEmail", teacherEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/teachingClasses").header("Authorization", token).header("x-userEmail", teacherEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        JSONObject obj = new JSONArray(bodyResult).getJSONObject(0);
        return gson.fromJson(obj.toString(), Clase.class);
    }

    protected String getJsonClase(Clase clase) {
        return String.format("{\"nombre\":\"%s\",\"capacity\":%d,\"description\":\"%s\",\"amountOfStudents\":%d,\"dateOfInit\":\"%s\",\"dateOfEnd\":\"%s\"}", clase.getNombre(), clase.getCapacity(), clase.getDescription(), clase.getAmountOfStudents(), getJsonFormatTimeStamp(clase.getDateOfInit()), getJsonFormatTimeStamp(clase.getDateOfEnd()));
    }

    protected String getJsonFormatTimeStamp(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat formatDate = new SimpleDateFormat(patternDate);
        SimpleDateFormat hourDate = new SimpleDateFormat(patternHour);
        return formatDate.format(date) + "T" + hourDate.format(date);
    }
}
