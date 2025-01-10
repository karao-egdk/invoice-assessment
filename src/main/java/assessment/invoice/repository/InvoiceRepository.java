package assessment.invoice.repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.dalesbred.Database;
import org.dalesbred.query.SqlQuery;

import assessment.invoice.dao.InvoiceDao;
import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.dto.UpdateInvoice;
import assessment.invoice.entity.Invoice;

public class InvoiceRepository implements InvoiceDao {
	private final Database database;

	public InvoiceRepository(Database database) {
		this.database = database;
	}

	@Override
	public Invoice insertInvoice(CreateInvoice invoice) {
		final String INSERT_INVOICE = "INSERT INTO invoice (amount, due_date, parent_id) VALUES (:amount, :dueDate, :parentId) RETURNING *";
		return database.findUnique(Invoice.class, SqlQuery.namedQuery(INSERT_INVOICE, invoice));
	}

	@Override
	public List<Invoice> getInvoices() {
		final String GET_INVOICES = "SELECT * FROM invoice";
		return database.findAll(Invoice.class, SqlQuery.query(GET_INVOICES));
	}

	@Override
	public Invoice updatePayment(UpdateInvoice payment) {
		final String UPDATE_PAYMENT = "UPDATE invoice SET paid_amount = :amount, status =:status WHERE id = :id RETURNING *";
		return database.findUnique(Invoice.class, SqlQuery.namedQuery(UPDATE_PAYMENT, payment));
	}

	@Override
	public Optional<Invoice> getInvoiceById(Integer id) {
		final String GET_INVOICES = "SELECT * FROM invoice WHERE id = ?";
		return database.findOptional(Invoice.class, SqlQuery.query(GET_INVOICES, id));
	}

	@Override
	public List<Invoice> getOverDueInvoices(Date overDueDate) {
		final String GET_OVERDUE = "SELECT * FROM invoice WHERE status = 'PENDING' AND due_date < ?";
		return database.findAll(Invoice.class, SqlQuery.query(GET_OVERDUE, overDueDate));
	}

	@Override
	public List<Invoice> getInvoicesById(Integer id) {
		final String GET_OVERDUE = "SELECT * FROM invoice WHERE id = :id OR parent_id = :id";

		Map<String, Integer> parameters = new HashMap<>();
		parameters.put("id", id);

		return database.findAll(Invoice.class, SqlQuery.namedQuery(GET_OVERDUE, parameters));
	}
}
