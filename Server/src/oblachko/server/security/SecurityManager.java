package oblachko.server.security;

public interface SecurityManager {
    void init();
    String getNick(String login, String pass);
    void dispose();
}
