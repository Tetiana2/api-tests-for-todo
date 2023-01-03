package me.tetiana;


import io.restassured.http.ContentType;
import io.restassured.http.Header;
import me.tetiana.dto.CreateTask;
import me.tetiana.dto.EmailLogin;
import me.tetiana.dto.SignUp;
import me.tetiana.dto.TaskData;
import org.junit.jupiter.api.*;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.apache.http.HttpStatus.*;
import static me.tetiana.util.Urls.*;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiTest {
    public static final String PROJECT_ID = "todo";

    public static final String DATABASE_ID = "todo";

    public static final String COLLECTION_ID = "todo";

    public static final Header PROJECT_HEADER = new Header("X-Appwrite-Project", PROJECT_ID);

    public static final String AUTH_COOKIE = format("a_session_%s", PROJECT_ID);

    private static String token;

    private static String taskId;

    private static final String EMAIL = "tetiana.babenko1@gmail.com";

    private static final String PASSWORD = "password";

    @BeforeAll
    public static void signUp() {
        given()
                .contentType(ContentType.JSON)
                .body(new SignUp("Tetiana", EMAIL, PASSWORD))
                .header(PROJECT_HEADER).
                when()
                .post(BASE_URL + ACCOUNT).
                then()
                .statusCode(SC_CREATED);
    }

    @Test
    @Order(1)
    public void login() {
        setToken(
                given()
                        .contentType(ContentType.JSON)
                        .body(new EmailLogin(EMAIL, PASSWORD))
                        .header(PROJECT_HEADER).
                        when()
                        .post(BASE_URL + EMAIL_LOGIN).
                        then()
                        .statusCode(SC_CREATED)
                        .body("providerUid", equalTo(EMAIL))
                        .extract()
                        .cookie(AUTH_COOKIE));
    }

    @Test
    @Order(2)
    public void createTask() {
        setTaskId(
                given()
                        .contentType(ContentType.JSON)
                        .body(CreateTask.from(new TaskData("Test task")))
                        .header(PROJECT_HEADER)
                        .cookie(AUTH_COOKIE, token).
                        when()
                        .post(BASE_URL + TASKS(DATABASE_ID, COLLECTION_ID)).
                        then()
                        .statusCode(SC_CREATED)
                        .body("content", equalTo("Test task"))
                        .body("isComplete", equalTo(false))
                        .extract()
                        .body()
                        .path("$id"));
    }

    @Test
    @Order(3)
    public void getTasks() {
        given()
                .header(PROJECT_HEADER)
                .cookie(AUTH_COOKIE, token).
                when()
                .get(BASE_URL + TASKS(DATABASE_ID, COLLECTION_ID)).
                then()
                .statusCode(SC_OK)
                .body("total", equalTo(1))
                .body("documents[0].$id", equalTo(taskId));
    }

    @Test
    @Order(4)
    public void markTaskAsDone() {
        given()
                .contentType(ContentType.JSON)
                .body(Map.of("data", Map.of("isComplete", true)))
                .header(PROJECT_HEADER)
                .cookie(AUTH_COOKIE, token).
                when()
                .patch(BASE_URL + TASK(DATABASE_ID, COLLECTION_ID, taskId)).
                then()
                .statusCode(SC_OK)
                .body("isComplete", equalTo(true));
    }

    @Test
    @Order(5)
    public void deleteTask() {
        given()
                .contentType(ContentType.JSON)
                .header(PROJECT_HEADER)
                .cookie(AUTH_COOKIE, token).
                when()
                .delete(BASE_URL + TASK(DATABASE_ID, COLLECTION_ID, taskId)).
                then()
                .statusCode(SC_NO_CONTENT);
    }

    @AfterAll
    public static void logout() {
        given()
                .header(PROJECT_HEADER)
                .cookie(AUTH_COOKIE, token).
                when()
                .delete(BASE_URL + LOGOUT).
                then()
                .statusCode(SC_NO_CONTENT);
    }

    private static void setToken(String auth) {
        token = auth;
    }

    private static void setTaskId(String taskId) {
        ApiTest.taskId = taskId;
    }
}
