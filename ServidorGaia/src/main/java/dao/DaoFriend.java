package dao;

public class DaoFriend {

    // send request  = INSERT INTO friends (username1,username2, value, request_date) VALUES (?, ?, 0, NOW()

    // accept request = UPDATE friends SET value = 1 WHERE username1 = ? AND username2 = ?

    // reject request = DELETE FROM friends WHERE username1 = ? AND username2 = ?

    // get friends = SELECT * FROM friends WHERE (username1 = ? OR username2 = ?) AND value = 1

    // get requests = SELECT * FROM friends WHERE username2 = ? AND value = 0

}
