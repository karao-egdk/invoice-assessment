package assessment.invoice.dao;

import java.util.List;
import java.util.Optional;

import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.dto.InvoicePayment;
import assessment.invoice.entity.Invoice;

public interface InvoiceDao {
	Invoice insertInvoice(CreateInvoice invoice);

	List<Invoice> getInvoices();

	Invoice updatePayment(InvoicePayment payment);

	Optional<Invoice> getInvoiceById(Integer id);
}
