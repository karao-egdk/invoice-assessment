package assessment.invoice.dao;

import java.util.List;

import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.entity.Invoice;

public interface InvoiceDao {
	Invoice insertInvoice(CreateInvoice invoice);

	List<Invoice> getInvoices();
}
