package hrsystem;

public final class EmployeeFactory {
	private EmployeeFactory() {
	}

	public static FullTimeEmployee createFullTime(String[] parts) {
		return new FullTimeEmployee(parts[0], parts[1], parts[2], Gender.valueOf(parts[3].trim().toUpperCase()),
				parts[4], parts[5], parts[6], parts[7], Integer.parseInt(parts[9]),
				EmploymentStatus.valueOf(parts[10].trim().toUpperCase().replace(' ', '_')), parts[11], parts[12],
				Double.parseDouble(parts[13]));
	}

	public static PartTimeEmployee createPartTime(String[] parts) {
		return new PartTimeEmployee(parts[0], parts[1], parts[2], Gender.valueOf(parts[3].trim().toUpperCase()),
				parts[4], parts[5], parts[6], parts[7], Integer.parseInt(parts[9]),
				EmploymentStatus.valueOf(parts[10].trim().toUpperCase().replace(' ', '_')), parts[11], parts[12],
				Integer.parseInt(parts[13]), Double.parseDouble(parts[14]));
	}

	public static ContractEmployee createContract(String[] parts) {
		return new ContractEmployee(parts[0], parts[1], parts[2], Gender.valueOf(parts[3].trim().toUpperCase()),
				parts[4], parts[5], parts[6], parts[7], Integer.parseInt(parts[9]),
				EmploymentStatus.valueOf(parts[10].trim().toUpperCase().replace(' ', '_')), parts[11], parts[12],
				Integer.parseInt(parts[13]), Double.parseDouble(parts[14]));
	}

	public static Employee create(String type, String id, String name, int deptId) {
		String dob = "";
		Gender gender = Gender.MALE;
		String email = "";
		String phone = "";
		String hireDate = "";
		String jobTitle = "";
		EmploymentStatus status = EmploymentStatus.ACTIVE;
		String lastPromotionDate = "";
		String password = "";

		return switch (type.toUpperCase()) {
		case "FT" -> new FullTimeEmployee(id, name, dob, gender, email, phone, hireDate, jobTitle, deptId, status,
				lastPromotionDate, password, 0.0);
		case "PT" -> new PartTimeEmployee(id, name, dob, gender, email, phone, hireDate, jobTitle, deptId, status,
				lastPromotionDate, password, 0, 0.0);
		case "CT" -> new ContractEmployee(id, name, dob, gender, email, phone, hireDate, jobTitle, deptId, status,
				lastPromotionDate, password, 0, 0.0);
		default -> throw new IllegalArgumentException("Unknown type: " + type);
		};
	}
}