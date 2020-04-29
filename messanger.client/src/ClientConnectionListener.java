public interface ClientConnectionListener {
    void sendMessage();
    void receivingMessage(String id, String sender, String recipient, String msg, String time, String flag);
    void getListOfMessages();

    // нужно доработать печать списка возможных друзей
    void friendSearch(String searchName);
    void printFriendList();
    void clearFriendsList();

    void printPossibleFriend(String name);
    void setStatusFriendSearchFalse();
    void runClient(String name);
}
