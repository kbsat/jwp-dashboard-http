package nextstep.org.apache.coyote.http11;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.jwp.presentation.Controller;
import nextstep.jwp.presentation.LoginController;
import nextstep.jwp.presentation.StaticResourceController;
import org.apache.coyote.http11.RequestMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestMappingTest {

    @Test
    @DisplayName("등록한 Path와 일치하는 Controller를 찾는다.")
    void findController() {
        // when
        final Controller controller = RequestMapping.findController("/login");

        // then
        assertThat(controller).isSameAs(LoginController.getInstance());
    }

    @Test
    @DisplayName("매핑되는 Controller가 없다면 StaticResourceController를 반환한다.")
    void findNotMappedController() {
        // when
        final Controller controller = RequestMapping.findController("/test");

        // then
        assertThat(controller).isSameAs(StaticResourceController.getInstance());
    }
}
