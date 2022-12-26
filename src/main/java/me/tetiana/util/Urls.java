package me.tetiana.util;

public class Urls {
    public static final String BASE_URL = "http://localhost:90/v1";

    public static final String EMAIL_LOGIN = "/account/sessions/email";

    public static final String ACCOUNT = "/account";

    public static final String LOGOUT = "/account/sessions/current";

    public static final String DATABASES = "/databases/";
    public static final String COLLECTIONS = "/collections/";
    public static final String DOCUMENTS = "/documents/";

    public static String TASKS(String databaseId, String collectionId) {
        return DATABASES + databaseId + COLLECTIONS + collectionId + DOCUMENTS;
    }

    public static String TASK(String databaseId, String collectionId, String taskId) {
        return TASKS(databaseId, collectionId) + taskId;
    }

}
