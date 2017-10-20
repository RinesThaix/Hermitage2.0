package mem.kitek.server.spring;

import mem.kitek.server.Bootstrap;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by RINES on 20.10.17.
 */
@Controller
@SpringBootApplication
@EnableAutoConfiguration
public class HeadController extends SpringBootServletInitializer implements ErrorController {
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container -> container.setPort(Bootstrap.getPort()));
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HeadController.class);
    }

    @RequestMapping(value="/", produces = {MediaType.TEXT_HTML_VALUE})
    public String getMainPage() {
        return "index";
    }

    @RequestMapping(value="/error", produces = {MediaType.TEXT_HTML_VALUE})
    public String getError() {
        return getMainPage();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
