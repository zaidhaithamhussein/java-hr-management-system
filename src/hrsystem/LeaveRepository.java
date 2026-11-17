package hrsystem;

import java.util.List;

public interface LeaveRepository {
	List<LeaveRequest> loadLeaves(String path);

	void saveLeaves(String path, List<LeaveRequest> leaves);
}