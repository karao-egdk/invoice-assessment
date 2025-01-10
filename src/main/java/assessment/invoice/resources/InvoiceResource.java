package assessment.invoice.resources;

import org.dalesbred.Database;

import assessment.invoice.dto.CreateInvoice;
import assessment.invoice.exception.NoDataException;
import assessment.invoice.service.InvoiceService;
import assessment.invoice.service.implementation.InvoiceServiceImplementation;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/invoices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InvoiceResource {
	private final InvoiceService service;

	public InvoiceResource(Database database) {
		this.service = new InvoiceServiceImplementation(database);
	}

	@POST
	public Response insertInvoice(CreateInvoice invoice) {
		try {
			return Response.ok(service.insertInvoice(invoice)).build();
		} catch (NoDataException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
		}
	}
}
