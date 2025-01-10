package assessment.invoice;

import org.dalesbred.Database;

import assessment.invoice.resources.InvoiceResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

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
		bootstrap.addBundle(new SwaggerBundle<invoiceConfiguration>() {
			@Override
			protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(invoiceConfiguration configuration) {
				return configuration.swaggerBundleConfiguration;
			}
		});
	}

	@Override
	public void run(final invoiceConfiguration configuration, final Environment environment) {

		DataSourceFactory factory = configuration.getDataSourceFactory();
		Database database = Database.forUrlAndCredentials(factory.getUrl(), factory.getUser(), factory.getPassword());

		environment.jersey().register(new InvoiceResource(database));
	}

}
