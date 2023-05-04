package domain.services;

public interface ServicesFriend {
    int sendRequest(String username1, String username2);

    void acceptRequest(String username1, String username2);

    void rejectRequest(String username1, String username2);

    void getFriends(String username);

    void getRequests(String username);
}
