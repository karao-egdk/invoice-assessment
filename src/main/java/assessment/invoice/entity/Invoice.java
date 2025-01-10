package assessment.invoice.entity;

import java.util.Date;

import assessment.invoice.enums.PayStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
	private Integer id;
	private Double amount;
	private Double paidAmount;
	private Date dueDate;
	private PayStatus status;
	private Integer parentId;

	public Invoice(Integer id, Double amount, Double paidAmount, Date dueDate, String status, Integer parentId) {
		this.id = id;
		this.amount = amount;
		this.paidAmount = paidAmount;
		this.dueDate = dueDate;
		this.status = PayStatus.valueOf(status);
		this.parentId = parentId;
	}

}
