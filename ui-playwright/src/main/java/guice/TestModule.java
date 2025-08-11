package guice;

import com.google.inject.AbstractModule;
import context.TestContext;
import io.cucumber.guice.ScenarioScoped;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TestContext.class).in(ScenarioScoped.class);
    }
}
