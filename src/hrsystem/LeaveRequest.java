package hrsystem;

public class LeaveRequest {
	private String leaveId;
	private String employeeId;
	private LeaveType leaveType;
	private String startDate;
	private String endDate;
	private ApprovalStatus approvalStatus;
	private String approverId;

	public LeaveRequest(String leaveId, String employeeId, LeaveType leaveType, String startDate, String endDate,
			ApprovalStatus approvalStatus, String approverId) {
		this.leaveId = leaveId;
		this.employeeId = employeeId;
		this.leaveType = leaveType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.approvalStatus = approvalStatus;
		this.approverId = approverId;
	}

	public ApprovalStatus getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(ApprovalStatus status) {
		this.approvalStatus = status;
	}

	public LeaveType getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(LeaveType leaveType) {
		this.leaveType = leaveType;
	}

	public String getLeaveId() {
		return leaveId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getApproverId() {
		return approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}
}
