package assessment.invoice.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.dto.UpdateInvoice;
import assessment.invoice.entity.Invoice;

public interface InvoiceDao {
	Invoice insertInvoice(CreateInvoice invoice);

	List<Invoice> getInvoices();

	Invoice updatePayment(UpdateInvoice payment);

	Optional<Invoice> getInvoiceById(Integer id);

	List<Invoice> getOverDueInvoices(Date overDueDate);
}
