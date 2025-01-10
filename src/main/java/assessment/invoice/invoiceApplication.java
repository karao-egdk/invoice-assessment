package assessment.invoice;

import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class invoiceApplication extends Application<invoiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new invoiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "invoice";
    }

    @Override
    public void initialize(final Bootstrap<invoiceConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final invoiceConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
