package ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ComponentScan(basePackages = {"ui", "services","config","helpers"})
@EnableVaadin
@Push
public class VaadinApplication implements AppShellConfigurator {
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        context = SpringApplication.run(VaadinApplication.class, args);
    }

    public static ConfigurableApplicationContext getContext() {
        return context;
    }
}
