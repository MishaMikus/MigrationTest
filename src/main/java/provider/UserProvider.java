package provider;

import model.UserModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class UserProvider {
    volatile private static Integer index = 0;

    private static List<String> userMigrationList;
    private static String userListFilePath;

    public synchronized static UserModel generateNewUser(String BASE_USER_NAME, String BASE_USER_PASSWORD) {
        index++;
        String postfix = index + "" + new Date().getTime() / ((index + 1) * 10);
        UserModel currentUserModel = new UserModel();
        currentUserModel.setEmail("any" + postfix + "@email.com");
        currentUserModel.setWebScreenName("w" + postfix);
        currentUserModel.setPassword("1111111q");
        currentUserModel.setBaseUserName(BASE_USER_NAME);
        currentUserModel.setBaseUserPassword(BASE_USER_PASSWORD);
        return currentUserModel;
    }

    public synchronized static UserModel migrationUser(String BASE_USER_NAME, String BASE_USER_PASSWORD, String USER_LIST_FILE_PATH) throws IOException {
        userListFilePath = USER_LIST_FILE_PATH;
        index++;
        UserModel currentUserModel = new UserModel();
        currentUserModel.setEmail(getUserMigrationList().get(index % getUserMigrationList().size()));
        currentUserModel.setPassword("1111111q");
        currentUserModel.setBaseUserName(BASE_USER_NAME);
        currentUserModel.setBaseUserPassword(BASE_USER_PASSWORD);
        return currentUserModel;
    }

    public static List<String> getUserMigrationList() throws IOException {
        if (userMigrationList == null) {
            Scanner s = new Scanner(new File(userListFilePath));
            userMigrationList = new ArrayList<>();
            while (s.hasNext()) {
                userMigrationList.add(s.next());
            }
            s.close();
        }
        return userMigrationList;
    }
}
