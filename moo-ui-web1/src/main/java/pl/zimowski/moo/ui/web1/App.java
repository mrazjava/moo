package pl.zimowski.moo.ui.web1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * UI processing backend entry point.
 *
 * @since 1.0.0
 * @author Adam Zimowski (<a href="mailto:mrazjava@yandex.com">mrazjava</a>)
 */
@SpringBootApplication
public class App {

    public static final String SESSION_ATTR_NICK = "nick";

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}