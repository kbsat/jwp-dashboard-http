package nextstep.jwp.presentation;

import org.apache.coyote.http11.common.HttpRequest;
import org.apache.coyote.http11.common.HttpResponse;

public class WelcomeController extends AbstractController {

    private static final WelcomeController INSTANCE = new WelcomeController();

    private static final String WELCOME_MESSAGE = "Hello world!";

    private WelcomeController() {
    }

    public static WelcomeController getInstance() {
        return INSTANCE;
    }

    @Override
    protected void doGet(final HttpRequest request, final HttpResponse response) {
        response.setBody(WELCOME_MESSAGE);
    }
}
