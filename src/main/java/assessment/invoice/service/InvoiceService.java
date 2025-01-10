package assessment.invoice.service;

import java.util.Map;

import assessment.invoice.dto.CreateInvoice;

public interface InvoiceService {
	Map<String, Integer> insertInvoice(CreateInvoice invoice) throws Exception;
}
