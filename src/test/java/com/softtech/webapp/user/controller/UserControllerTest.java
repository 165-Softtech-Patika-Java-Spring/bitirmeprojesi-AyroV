package com.softtech.webapp.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.softtech.webapp.WebApplication;
import com.softtech.webapp.config.H2TestProfileJPAConfig;
import com.softtech.webapp.user.dto.UserDeleteDto;
import com.softtech.webapp.user.dto.UserPatchDto;
import com.softtech.webapp.user.dto.UserPostDto;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.*;

import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {WebApplication.class, H2TestProfileJPAConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    private static final String BASE_PATH = "/api/v1/users";
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    private ObjectMapper objectMapper;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    @BeforeEach
    void setUpEach() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        Class.forName("org.h2.Driver");
        this.connection = DriverManager.getConnection ("jdbc:h2:mem:db", "sa","");
        this.statement = connection.createStatement();
        this.resultSet = null;
    }

    @AfterEach
    void tearDownEach() throws Exception {
        if (resultSet != null){
            resultSet.close();
            resultSet = null;
        }

        statement.close();

        statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM USERS");
        statement.close();
    }

    @AfterAll
    void tearDownAll() throws Exception {
        this.connection.close();
    }

    @Test
    void saveSuccess() throws Exception {
        UserPostDto userPostDto = UserPostDto.builder()
                .username("username-save")
                .name("name-save")
                .surname("surname-save")
                .password("password")
                .build();

        String content = objectMapper.writeValueAsString(userPostDto);

        MvcResult result = mockMvc.perform(
                post(BASE_PATH).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(201, result.getResponse().getStatus());

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM USERS");
        resultSet.next();

        assertEquals("username-save", resultSet.getString("username"));
        assertEquals("name-save", resultSet.getString("name"));
        assertEquals("surname-save", resultSet.getString("surname"));
    }

    @Test
    void saveWithTakenUsernameWithCase() throws Exception {
        UserPostDto userPostDto = UserPostDto.builder()
                .username("username-save")
                .name("name-save")
                .surname("surname-save")
                .password("password")
                .build();

        String content = objectMapper.writeValueAsString(userPostDto);

        MvcResult result = mockMvc.perform(
                post(BASE_PATH).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        result = mockMvc.perform(
                post(BASE_PATH).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains(
                "\"message\":\"This username is already exists!\","
        ));
    }

    @Test
    void saveWithTakenUsernameWithoutCase() throws Exception {
        UserPostDto userPostDto = UserPostDto.builder()
                .username("uSerNamE-saVe")
                .name("name-save")
                .surname("surname-save")
                .password("password")
                .build();

        String content = objectMapper.writeValueAsString(userPostDto);

        MvcResult result = mockMvc.perform(
                post(BASE_PATH).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        result = mockMvc.perform(
                post(BASE_PATH).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains(
                "\"message\":\"This username is already exists!\","
        ));
    }

    @Test
    void saveEmptyRequiredFieldUsername() throws Exception {
        UserPostDto userPostDto = UserPostDto.builder()
                .name("name-save")
                .surname("surname-save")
                .password("password")
                .build();

        String content = objectMapper.writeValueAsString(userPostDto);

        MvcResult result = mockMvc.perform(
                post(BASE_PATH).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(
                result.getResponse().getContentAsString().compareTo("{\"username\":\"Username cannot be null\"}") == 0
                ||
                result.getResponse().getContentAsString().compareTo("{\"username\":\"Username cannot be blank\"}") == 0
        );
    }

    @Test
    void saveEmptyRequiredFieldName() throws Exception {
        UserPostDto userPostDto = UserPostDto.builder()
                .username("username-save")
                .surname("surname-save")
                .password("password")
                .build();

        String content = objectMapper.writeValueAsString(userPostDto);

        MvcResult result = mockMvc.perform(
                post(BASE_PATH).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(
                result.getResponse().getContentAsString().compareTo("{\"name\":\"Name cannot be null\"}") == 0
                ||
                result.getResponse().getContentAsString().compareTo("{\"name\":\"Name cannot be blank\"}") == 0
        );
    }

    @Test
    void saveEmptyRequiredFieldSurname() throws Exception {
        UserPostDto userPostDto = UserPostDto.builder()
                .username("username-save")
                .name("name-save")
                .password("password")
                .build();

        String content = objectMapper.writeValueAsString(userPostDto);

        MvcResult result = mockMvc.perform(
                post(BASE_PATH).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(
                result.getResponse().getContentAsString().compareTo("{\"surname\":\"Surname cannot be null\"}") == 0
                        ||
                        result.getResponse().getContentAsString().compareTo("{\"surname\":\"Surname cannot be blank\"}") == 0
        );
    }

    @Test
    void saveEmptyRequiredFieldPassword() throws Exception {
        UserPostDto userPostDto = UserPostDto.builder()
                .username("username-save")
                .name("name-save")
                .surname("surname-save")
                .build();

        String content = objectMapper.writeValueAsString(userPostDto);

        MvcResult result = mockMvc.perform(
                post(BASE_PATH).content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(
                result.getResponse().getContentAsString().compareTo("{\"password\":\"Password cannot be null\"}") == 0
                        ||
                        result.getResponse().getContentAsString().compareTo("{\"password\":\"Password cannot be blank\"}") == 0
        );
    }

    @Test
    void findAllWithData() throws Exception {
        statement = connection.createStatement();
        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-1', 'password-save-1', 'surname-save-1', 'username-save-1', 'USERNAME-SAVE-1', 1L)"
        );

        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-2', 'password-save-2', 'surname-save-2', 'username-save-2', 'USERNAME-SAVE-2', 2L)"
        );

        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-3', 'password-save-3', 'surname-save-3', 'username-save-3', 'USERNAME-SAVE-3', 3L)"
        );

        MvcResult result = mockMvc.perform(
                get(BASE_PATH).content("").contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[" +
                "{\"username\":\"username-save-1\",\"name\":\"name-save-1\",\"surname\":\"surname-save-1\"}," +
                "{\"username\":\"username-save-2\",\"name\":\"name-save-2\",\"surname\":\"surname-save-2\"}," +
                "{\"username\":\"username-save-3\",\"name\":\"name-save-3\",\"surname\":\"surname-save-3\"}" +
                "]",
                result.getResponse().getContentAsString()
        );
    }

    @Test
    void findAllWithDataUsername() throws Exception {
        statement = connection.createStatement();
        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-1', 'password-save-1', 'surname-save-1', 'username-save-1', 'USERNAME-SAVE-1', 1L)"
        );

        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-2', 'password-save-2', 'surname-save-2', 'username-save-2', 'USERNAME-SAVE-2', 2L)"
        );

        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-3', 'password-save-3', 'surname-save-3', 'username-save-3', 'USERNAME-SAVE-3', 3L)"
        );

        MvcResult result = mockMvc.perform(
                get(BASE_PATH + "?username=username-save-2").content("").contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(
                "[{\"username\":\"username-save-2\",\"name\":\"name-save-2\",\"surname\":\"surname-save-2\"}]",
                result.getResponse().getContentAsString()
        );
    }

    @Test
    void findAllWithoutData() throws Exception {
        MvcResult result = mockMvc.perform(
                get(BASE_PATH).content("").contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    void findAllWithoutDataUsername() throws Exception {
        MvcResult result = mockMvc.perform(
                get(BASE_PATH + "?username=username-save-2").content("").contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(404, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains(
                "\"message\":\"Item not found! -> User\""
        ));
    }

    @Test
    void findById() throws Exception {
        statement = connection.createStatement();
        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-1', 'password-save-1', 'surname-save-1', 'username-save-1', 'USERNAME-SAVE-1', 1L)"
        );

        MvcResult result = mockMvc.perform(
                get(BASE_PATH + "/1").content("1").contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());
        assertEquals(
                "{\"username\":\"username-save-1\",\"name\":\"name-save-1\",\"surname\":\"surname-save-1\"}",
                result.getResponse().getContentAsString()
        );
    }

    @Test
    void findByIdFail() throws Exception {
        MvcResult result = mockMvc.perform(
                get(BASE_PATH + "/1").content("1").contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(404, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains(
                "\"message\":\"Item not found! -> User\""
        ));
    }

    @Test
    void updateSuccess() throws Exception {
        statement = connection.createStatement();
        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-1', 'password-save-1', 'surname-save-1', 'username-save-1', 'USERNAME-SAVE-1', 1L)"
        );

        statement.close();
        UserPatchDto userPatchDto = UserPatchDto.builder()
                .username("username-patch")
                .name("name-patch")
                .surname("surname-patch")
                .password("password-patch")
                .build();

        String content = objectMapper.writeValueAsString(userPatchDto);

        MvcResult result = mockMvc.perform(
                patch(BASE_PATH + "/1").content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(200, result.getResponse().getStatus());

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT * FROM USERS WHERE user_id=1");
        resultSet.next();

        assertEquals("username-patch", resultSet.getString("username"));
        assertEquals("name-patch", resultSet.getString("name"));
        assertEquals("surname-patch", resultSet.getString("surname"));
    }

    @Test
    void updateNoUser() throws Exception {
        UserPatchDto userPatchDto = UserPatchDto.builder()
                .username("username-patch")
                .name("name-patch")
                .surname("surname-patch")
                .password("password-patch")
                .build();

        String content = objectMapper.writeValueAsString(userPatchDto);

        MvcResult result = mockMvc.perform(
                patch(BASE_PATH + "/1").content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(404, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains(
                "\"message\":\"Item not found! -> User\""
        ));
    }

    @Test
    void updateWithTakenUsername() throws Exception {
        statement = connection.createStatement();
        statement.executeUpdate(
                "insert into users " +
                 "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                 "values (null, null, null, null, 'name-save-1', 'password-save-1', 'surname-save-1', 'username-save-1', 'USERNAME-SAVE-1', 1L)"
        );

        UserPatchDto userPatchDto = UserPatchDto.builder()
                .username("username-save-1")
                .name("name-patch")
                .surname("surname-patch")
                .password("password-patch")
                .build();

        String content = objectMapper.writeValueAsString(userPatchDto);

        MvcResult result = mockMvc.perform(
                patch(BASE_PATH + "/1").content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains(
                "\"message\":\"This username is already exists!\""
        ));
    }

    @Test
    void deleteSuccess() throws Exception {
        statement = connection.createStatement();
        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-1', 'password-save-1', 'surname-save-1', 'username-save-1', 'USERNAME-SAVE-1', 1L)"
        );
        statement.close();

        UserDeleteDto userDeleteDto = UserDeleteDto.builder()
                .username("username-save-1")
                .build();

        String content = objectMapper.writeValueAsString(userDeleteDto);

        MvcResult result = mockMvc.perform(
                delete(BASE_PATH + "/1").content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(204, result.getResponse().getStatus());

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT COUNT(*) as row_count FROM USERS WHERE user_id=1");
        resultSet.next();
        assertEquals(0, resultSet.getInt("row_count"));
    }

    @Test
    void deleteNoUser() throws Exception {
        UserDeleteDto userDeleteDto = UserDeleteDto.builder()
                .username("username-save-1")
                .build();

        String content = objectMapper.writeValueAsString(userDeleteDto);

        MvcResult result = mockMvc.perform(
                delete(BASE_PATH + "/1").content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(404, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains(
                "\"message\":\"Item not found! -> User\""
        ));
    }

    @Test
    void deleteIdAndUserNameNotMatch() throws Exception {
        statement = connection.createStatement();
        statement.executeUpdate(
                "insert into users " +
                "(create_date, created_by, update_date, updated_by, name, password, surname, username, username_upper, user_id) " +
                "values (null, null, null, null, 'name-save-1', 'password-save-1', 'surname-save-1', 'username-save-1', 'USERNAME-SAVE-1', 1L)"
        );
        statement.close();

        UserDeleteDto userDeleteDto = UserDeleteDto.builder()
                .username("username-save-1")
                .build();

        String content = objectMapper.writeValueAsString(userDeleteDto);

        MvcResult result = mockMvc.perform(
                delete(BASE_PATH + "/2").content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().contains(
                "\"message\":\"Given id and username doesn't match\""
        ));

        statement = connection.createStatement();
        resultSet = statement.executeQuery("SELECT COUNT(*) as row_count FROM USERS WHERE user_id=1");
        resultSet.next();
        assertEquals(1, resultSet.getInt("row_count"));
    }

    @Test
    void deleteEmptyRequiredFields() throws Exception {
        UserDeleteDto userDeleteDto = UserDeleteDto.builder().build();

        String content = objectMapper.writeValueAsString(userDeleteDto);

        MvcResult result = mockMvc.perform(
                delete(BASE_PATH + "/2").content(content).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();

        assertEquals(400, result.getResponse().getStatus());
        assertTrue(
                result.getResponse().getContentAsString().compareTo("{\"username\":\"Username cannot be null\"}") == 0
                ||
                result.getResponse().getContentAsString().compareTo("{\"username\":\"Username cannot be blank\"}") == 0
        );
    }
}
