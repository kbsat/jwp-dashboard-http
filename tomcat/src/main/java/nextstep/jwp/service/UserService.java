package nextstep.jwp.service;

import java.util.Objects;
import java.util.Optional;
import nextstep.jwp.db.InMemoryUserRepository;
import nextstep.jwp.exception.UserNotFoundException;
import nextstep.jwp.model.User;
import org.apache.coyote.http11.common.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {

    private static final UserService INSTANCE = new UserService();

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private static final String ACCOUNT_PARAM = "account";
    private static final String EMAIL_PARAM = "email";
    private static final String PASSWORD_PARAM = "password";

    private UserService() {
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    public User login(final HttpRequest request) {
        final String account = request.getBodyParam(ACCOUNT_PARAM);
        final String password = request.getBodyParam(PASSWORD_PARAM);

        checkNull(account, password);

        final User user = InMemoryUserRepository.findByAccount(account)
                .orElseThrow(UserNotFoundException::new);
        if (!user.checkPassword(password)) {
            throw new IllegalStateException("비밀번호가 틀렸습니다.");
        }

        logSuccessMessage(user);
        return user;
    }

    public User register(final HttpRequest request) {
        final String account = request.getBodyParam(ACCOUNT_PARAM);
        final String email = request.getBodyParam(EMAIL_PARAM);
        final String password = request.getBodyParam(PASSWORD_PARAM);

        checkNull(account, email, password);
        checkAccountDuplication(account);

        final User user = new User(account, password, email);
        InMemoryUserRepository.save(user);

        logSuccessMessage(user);
        return user;
    }

    private void logSuccessMessage(final User user) {
        final String msg = user.toString();
        LOG.info(msg);
    }

    private void checkNull(final String... params) {
        for (final String param : params) {
            Objects.requireNonNull(param);
        }
    }

    private void checkAccountDuplication(final String account) {
        final Optional<User> savedAccount = InMemoryUserRepository.findByAccount(account);
        if (savedAccount.isPresent()) {
            throw new IllegalStateException("중복된 아이디입니다.");
        }
    }
}
