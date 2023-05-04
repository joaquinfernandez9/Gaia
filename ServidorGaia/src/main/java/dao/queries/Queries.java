package dao.queries;

public class Queries {

    public static final String SELECT_FROM_TREE_WHERE_USERNAME = "select * from tree where username = ?";
    public static final String UPDATE_TREE_SET_LEVEL_LEVEL_1_PROGRESS_0_WHERE_USERNAME = "UPDATE tree SET level = level+1, progress = 0 WHERE username = ?";
    public static final String INSERT_INTO_TASK_ID_TASK_NAME_INITIAL_TIME_END_TIME_COMPLETED_USERNAME_VALUES_0 = "insert into task (id, task_name, initial_time, end_time, completed, username) values (?, ?, ?, ?, 0, ?)";
    public static final String SELECT_FROM_TASK_WHERE_TASK_NAME_AND_USERNAME = "select * from task where task_name = ? and username = ?";
    public static final String DELETE_FROM_TASK_WHERE_TASK_NAME_AND_USERNAME = "delete from task where task_name = ? and username = ?";
    public static final String SELECT_FROM_TASK_WHERE_USERNAME = "select * from task where username = ?";
    public static final String UPDATE_TASK_SET_COMPLETED_1_WHERE_TASK_NAME_AND_USERNAME = "update task set completed = 1 where task_name = ? and username = ?";
    public static final String UPDATE_TREE_SET_PROGRESS_PROGRESS_1_WHERE_USERNAME = "update tree set progress = progress + 1 where username = ?";
    public static final String DELETE_FROM_TASK_WHERE_COMPLETED_1_AND_USERNAME = "delete from task where completed = 1 and username = ?";
    public static final String SELECT_FROM_ACCOUNT_WHERE_USERNAME_AND_PASSWORD = "SELECT * from account where username = ? and password = ?";
    public static final String SELECT_PASSWORD_ACTIVATED_FROM_ACCOUNT_WHERE_USERNAME = "SELECT password, activated from account where username = ?";
    public static final String INSERT_INTO_ACCOUNT_EMAIL_PASSWORD_USERNAME_ACTIVATED_ACTIVATION_CODE_ACTIVATION_TIME_VALUES = "INSERT INTO account (email, password, username, activated, activation_code, activation_time) values (?,?,?,?,?,?)";
    public static final String SELECT_FROM_ACCOUNT_WHERE_ACTIVATION_CODE_AND_ACTIVATION_TIME = "SELECT * from account where activation_code = ? and activation_time = ?";
    public static final String UPDATE_ACCOUNT_SET_ACTIVATED_1_WHERE_ACTIVATION_CODE = "UPDATE account set activated = 1 where activation_code = ?";
    public static final String INSERT_INTO_FRIENDS_USERNAME_1_USERNAME_2_VALUE_REQUEST_DATE_VALUES_0_NOW = "INSERT INTO friends (username1,username2, value, request_date) VALUES (?, ?, 0, NOW())";
    public static final String UPDATE_FRIENDS_SET_VALUE_1_WHERE_USERNAME_1_AND_USERNAME_2 = "UPDATE friends SET value = 1 WHERE username1 = ? AND username2 = ?";
    public static final String DELETE_FROM_FRIENDS_WHERE_USERNAME_1_AND_USERNAME_2 = "DELETE FROM friends WHERE username1 = ? AND username2 = ?";
    public static final String SELECT_FROM_FRIENDS_WHERE_USERNAME_1_OR_USERNAME_2_AND_VALUE_1 = "SELECT * FROM friends WHERE (username1 = ? OR username2 = ?) AND value = 1";
    public static final String SELECT_FROM_FRIENDS_WHERE_USERNAME_2_AND_VALUE_0 = "SELECT * FROM friends WHERE username2 = ? AND value = 0";
}