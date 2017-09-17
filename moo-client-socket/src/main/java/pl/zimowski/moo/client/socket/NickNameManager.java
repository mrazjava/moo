package pl.zimowski.moo.client.socket;

import org.springframework.stereotype.Component;

/**
 * Tracks state of users nixk name. Nick name can be defined by the user but
 * is optional, in which case it will be defined (randomly) by the server.
 * This manager allows to set nick name from different components depending on
 * how it was generated.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@Component
public class NickNameManager implements NickNameAssigning {

    private String nickName;


    public String getNickName() {
        return nickName;
    }

    @Override
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
