package pl.zimowski.moo.client.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import pl.zimowski.moo.client.socket.NickNameManager;

public class NickNameManagerTest {

    @Test
    public void shouldManageNickName() {

        NickNameManager mgr = new NickNameManager();

        assertNull(mgr.getNickName());
        mgr.setNickName("foo bar");
        assertEquals("foo bar", mgr.getNickName());
    }
}
