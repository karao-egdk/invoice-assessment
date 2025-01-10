package assessment.invoice.service;

import java.util.List;
import java.util.Map;

import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.entity.Invoice;

public interface InvoiceService {
	Map<String, Integer> insertInvoice(CreateInvoice invoice) throws Exception;
	
	List<Invoice> getInvoices();
}
