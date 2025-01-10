package assessment.invoice.dao;

import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.entity.Invoice;

public interface InvoiceDao {
	Invoice insertInvoice(CreateInvoice invoice);
}
