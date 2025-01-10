package assessment.invoice.repository;

import java.util.List;

import org.dalesbred.Database;
import org.dalesbred.query.SqlQuery;

import assessment.invoice.dao.InvoiceDao;
import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.entity.Invoice;

public class InvoiceRepository implements InvoiceDao {
	private final Database database;

	public InvoiceRepository(Database database) {
		this.database = database;
	}

	@Override
	public Invoice insertInvoice(CreateInvoice invoice) {
		final String INSERT_INVOICE = "INSERT INTO invoice (amount, due_date) VALUES (:amount, :dueDate) RETURNING *";
		return database.findUnique(Invoice.class, SqlQuery.namedQuery(INSERT_INVOICE, invoice));
	}

	@Override
	public List<Invoice> getInvoices() {
		final String GET_INVOICES = "SELECT * FROM INVOICE";
		return database.findAll(Invoice.class, SqlQuery.query(GET_INVOICES));
	}
}
