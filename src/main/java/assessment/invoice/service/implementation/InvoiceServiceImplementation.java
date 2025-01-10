package assessment.invoice.service.implementation;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.time.DateUtils;
import org.dalesbred.Database;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import assessment.invoice.dao.InvoiceDao;
import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.dto.ProcessOverdue;
import assessment.invoice.dto.UpdateInvoice;
import assessment.invoice.entity.Invoice;
import assessment.invoice.enums.PayStatus;
import assessment.invoice.exception.InvalidDataException;
import assessment.invoice.exception.NoDataException;
import assessment.invoice.repository.InvoiceRepository;
import assessment.invoice.service.InvoiceService;

public class InvoiceServiceImplementation implements InvoiceService {
	private final InvoiceDao repository;
	private final String DATABASE_SOURCE = "db/tables.sql";

	private String generateTableIfNotExists() throws IOException {
		URL url = Resources.getResource(DATABASE_SOURCE);
		String tables = Resources.toString(url, Charsets.UTF_8);
		return tables;
	}

	public InvoiceServiceImplementation(Database database) {
		this.repository = new InvoiceRepository(database);

		try {
			database.update(generateTableIfNotExists());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Integer> insertInvoice(CreateInvoice invoice) throws Exception {
		if (invoice == null || invoice.getAmount() == null || invoice.getDueDate() == null)
			throw new NoDataException("Some of the data is missing");

		if (invoice.getAmount() <= 0)
			throw new InvalidDataException("Please add appropriate amount");

		Invoice insertedInvoice = repository.insertInvoice(invoice);

		Map<String, Integer> res = new HashMap<>();
		res.put("id", insertedInvoice.getId());

		return res;
	}

	@Override
	public List<Invoice> getInvoices() {
		return repository.getInvoices();
	}

	@Override
	public Invoice updatePayment(UpdateInvoice payment) throws Exception {
		if (payment == null || payment.getAmount() == null || payment.getId() == null)
			throw new NoDataException("Some of the data is missing");

		Optional<Invoice> invoice = repository.getInvoiceById(payment.getId());

		if (invoice.isEmpty())
			return null;

		if (payment.getAmount() <= 0 || invoice.get().getAmount() < payment.getAmount())
			throw new InvalidDataException(
					"Amount being paid is either negative or is greater than the amount expected to pay");

		double totalPaid = invoice.get().getPaidAmount() + payment.getAmount();
		if (totalPaid > invoice.get().getAmount())
			throw new InvalidDataException("Cannot pay more than the actual amount");

		PayStatus updatedStatus = totalPaid == invoice.get().getAmount() ? PayStatus.PAID : PayStatus.PENDING;

		payment.setAmount(totalPaid);
		payment.setStatus(updatedStatus);

		return repository.updatePayment(payment);
	}

	@Override
	public Map<String, Object> processOverdue(ProcessOverdue overdue) throws Exception {
		if (overdue == null || overdue.getLateFee() == null || overdue.getOverdueDays() == null)
			throw new NoDataException("Some of the data is missing");

		if (overdue.getLateFee() < 0 || overdue.getOverdueDays() < 0)
			throw new InvalidDataException("Data cannot be in negative");

		Date currDate = new Date();
		List<Invoice> overdueInvoices = repository.getOverDueInvoices(currDate);
		List<Invoice> insertedInvoice = updateStatusAndProcessOverdue(overdueInvoices, overdue, currDate);

		Map<String, Object> res = new HashMap<>();
		res.put("overdue_invoices", overdueInvoices);
		res.put("inserted_invoices", insertedInvoice);

		return res;
	}

	@Override
	public List<Invoice> getInvoicesById(Integer id) throws Exception {
		if (id == null)
			throw new NoDataException("Some of the data is missing");

		return repository.getInvoicesById(id);
	}

	private List<Invoice> updateStatusAndProcessOverdue(List<Invoice> overdues, ProcessOverdue processOverdue,
			Date currDate) {
		List<Invoice> insertedInvoice = new ArrayList<>();

		overdues.forEach(overdue -> {
			PayStatus status = overdue.getPaidAmount() == 0 ? PayStatus.VOID : PayStatus.PAID;

			// Update Invoice status
			UpdateInvoice updateInvoice = new UpdateInvoice(overdue.getId(), overdue.getPaidAmount(), status);
			repository.updatePayment(updateInvoice);

			// Insert new Invoice
			double newAmount = overdue.getAmount() - overdue.getPaidAmount() + processOverdue.getLateFee();
			Date newOverdueDate = DateUtils.addDays(currDate, processOverdue.getOverdueDays());
			int parentId = overdue.getParentId() == null ? overdue.getId() : overdue.getParentId();

			CreateInvoice createInvoice = new CreateInvoice(newAmount, newOverdueDate, parentId);
			insertedInvoice.add(repository.insertInvoice(createInvoice));
		});

		return insertedInvoice;
	}

}
