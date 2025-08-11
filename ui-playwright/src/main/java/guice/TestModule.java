package guice;

import com.google.inject.AbstractModule;
import context.TestContext;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TestContext.class).asEagerSingleton();
    }
}
