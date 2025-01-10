package assessment.invoice.dto;

import assessment.invoice.enums.PayStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInvoice {
	private Integer id;
	private Double amount;
	private PayStatus status;
	private Integer parentId;
}
