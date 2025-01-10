package assessment.invoice.resources;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.dalesbred.Database;
import org.dalesbred.junit.TestDatabaseProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.dto.ProcessOverdue;
import assessment.invoice.dto.UpdateInvoice;
import assessment.invoice.entity.Invoice;
import assessment.invoice.exception.InvalidDataException;
import assessment.invoice.exception.NoDataException;
import assessment.invoice.service.InvoiceService;
import assessment.invoice.service.implementation.InvoiceServiceImplementation;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
public class InvoiceResource {
	private static Database db = TestDatabaseProvider.databaseForProperties("db.properties");
	public static InvoiceService service;

	@BeforeAll
	public static void setup() {
		service = new InvoiceServiceImplementation(db);
		db.update("INSERT INTO invoice (amount, due_date) VALUES (300, '2024-01-01');");
	}

	@AfterAll
	public static void done() {
		db.update("TRUNCATE TABLE invoice RESTART IDENTITY");
	}

	@Test
	public void insertInvoices() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2024);
		calendar.set(Calendar.MONTH, Calendar.JUNE);
		calendar.set(Calendar.DAY_OF_MONTH, 17);

		CreateInvoice createInvoice = new CreateInvoice(300D, calendar.getTime(), null);
		Map<String, Integer> id = service.insertInvoice(createInvoice);

		assertThat(id.get("id")).isEqualTo(3);

	}

	@Test
	public void noAmountInsert() throws Exception {
		CreateInvoice createInvoice = new CreateInvoice(null, null, null);
		createInvoice.setAmount(null);

		assertThrows(NoDataException.class, () -> service.insertInvoice(createInvoice));
	}

	@Test
	public void noDateInsert() throws Exception {
		CreateInvoice createInvoice = new CreateInvoice(300D, null, null);
		createInvoice.setAmount(null);

		assertThrows(NoDataException.class, () -> service.insertInvoice(createInvoice));
	}

	@Test
	public void invalidDataError() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 2024);
		calendar.set(Calendar.MONTH, Calendar.JUNE);
		calendar.set(Calendar.DAY_OF_MONTH, 17);

		CreateInvoice createInvoice = new CreateInvoice(-300D, calendar.getTime(), null);
		createInvoice.setAmount(null);

		assertThrows(NoDataException.class, () -> service.insertInvoice(createInvoice));
	}

	@Test
	public void getInvoices() {
		List<Invoice> invoices = service.getInvoices();

		assertThat(invoices.size()).isGreaterThan(0);
	}

	@Test
	public void updateAmount() throws Exception {
		UpdateInvoice invoice = new UpdateInvoice(1, 150D, null);

		Invoice updated = service.updatePayment(invoice);

		assertThat(updated.getPaidAmount()).isEqualTo(150);

	}

	@Test
	public void updatePaymentAmountNull() throws Exception {
		UpdateInvoice invoice = new UpdateInvoice(1, null, null);

		assertThrows(NoDataException.class, () -> service.updatePayment(invoice));

	}

	@Test
	public void updatePaymentIdNull() throws Exception {
		UpdateInvoice invoice = new UpdateInvoice(null, 150D, null);

		assertThrows(NoDataException.class, () -> service.updatePayment(invoice));

	}

	@Test
	public void updatePaymentNull() throws Exception {
		assertThrows(NoDataException.class, () -> service.updatePayment(null));

	}

	@Test
	public void updateInvalidAmount() throws Exception {
		UpdateInvoice invoice = new UpdateInvoice(1, -150D, null);

		assertThrows(InvalidDataException.class, () -> service.updatePayment(invoice));

	}

	@Test
	public void updateInvalidAmountGreater() throws Exception {
		UpdateInvoice invoice = new UpdateInvoice(1, 250D, null);

		assertThrows(InvalidDataException.class, () -> service.updatePayment(invoice));

	}

	@Test
	public void updatePaymentPayMoreThanRemaining() throws Exception {
		UpdateInvoice invoice = new UpdateInvoice(1, 550D, null);

		assertThrows(InvalidDataException.class, () -> service.updatePayment(invoice));

	}

	@Test
	public void overdue() throws Exception {
		ProcessOverdue overdue = new ProcessOverdue(100D, 10);

		assertDoesNotThrow(() -> service.processOverdue(overdue));
	}

	@Test
	public void overdueNoData() throws Exception {
		assertThrows(NoDataException.class, () -> service.processOverdue(null));
	}

	@Test
	public void overdueNoLateFee() throws Exception {
		ProcessOverdue overdue = new ProcessOverdue(null, 10);

		assertThrows(NoDataException.class, () -> service.processOverdue(overdue));
	}

	@Test
	public void overdueNoDueDays() throws Exception {
		ProcessOverdue overdue = new ProcessOverdue(100D, null);

		assertThrows(NoDataException.class, () -> service.processOverdue(overdue));
	}
	
	@Test
	public void overdueInvalidFee() throws Exception {
		ProcessOverdue overdue = new ProcessOverdue(-100D, 10);

		assertThrows(InvalidDataException.class, () -> service.processOverdue(overdue));
	}
	
	@Test
	public void overdueInvalidDueDays() throws Exception {
		ProcessOverdue overdue = new ProcessOverdue(100D, -10);

		assertThrows(InvalidDataException.class, () -> service.processOverdue(overdue));
	}
}
