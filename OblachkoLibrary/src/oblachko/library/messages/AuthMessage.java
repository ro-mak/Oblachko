package oblachko.library.messages;


import oblachko.network.ServiceMessage;

import java.io.Serializable;

public class AuthMessage implements ServiceMessage,Serializable {
    private String login;
    private String password;

    public AuthMessage(String login, String password){
        this.login = login;
        this.password = password;

    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
