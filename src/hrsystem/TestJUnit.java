package hrsystem;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.FileWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class TestJUnit {

	@TempDir
	Path tmp;

	private Path ftFile, ptFile, ctFile, deptFile, leaveFile, payrollFile;

	private HRFacade facade;

	@BeforeEach
	void setup() throws IOException {

		ftFile = tmp.resolve("employees_full_time.txt");
		ptFile = tmp.resolve("employees_part_time.txt");
		ctFile = tmp.resolve("employees_contract.txt");
		deptFile = tmp.resolve("departments.txt");
		leaveFile = tmp.resolve("leaves.txt");
		payrollFile = tmp.resolve("payroll.txt");

		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(ftFile))) {
			out.println(
					"employeeId\tfullName\tdateOfBirth\tgender\temail\tphoneNumber\thireDate\tjobTitle\tCOL8\tdepartmentId\tstatus\tlastPromotionDate\tpassword\tsalary");

			out.println(
					"E001\tJohn Smith\t1990-01-01\tMALE\tjohn@co.com\t0501111222\t2015-06-01\tHR Officer\t\t1\tACTIVE\t2024-01-01\tJE001\t9000.00");
		}

		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(ptFile))) {
			out.println(
					"employeeId\tfullName\tdateOfBirth\tgender\temail\tphoneNumber\thireDate\tjobTitle\tCOL8\tdepartmentId\tstatus\tlastPromotionDate\tpassword\thoursPerMonth\thourlyRate");

		}

		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(ctFile))) {
			out.println(
					"employeeId\tfullName\tdateOfBirth\tgender\temail\tphoneNumber\thireDate\tjobTitle\tCOL8\tdepartmentId\tstatus\tlastPromotionDate\tpassword\tdurationMonths\toverallPayment");

		}

		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(deptFile))) {
			out.println("departmentId\tdepartmentName\theadOfDepartmentId\tbudget\tlocation");
			out.println("1\tHR\tE001\t100000\tHQ");
			out.println("2\tEngineering\t-\t500000\tHQ2");
		}

		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(leaveFile))) {
			out.println("leaveId\temployeeId\tleaveType\tstartDate\tendDate\tapprovalStatus\tapproverId");

		}

		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(payrollFile))) {
			out.println(
					"payrollId\temployeeId\tmonth\tbaseSalary\tbonuses\tdeductions\tnetSalary\ttaxWithheld\tprocessedDate");

		}

		facade = new HRFacade();
		facade.loadAll(ftFile.toString(), ptFile.toString(), ctFile.toString(), deptFile.toString(),
				leaveFile.toString(), payrollFile.toString());
	}

	@Test
	void TC_LOGIN_01_validLogin() {
		Employee e = facade.login("E001", "JE001");
		assertNotNull(e, "Expected successful login with correct credentials");
		assertEquals("John Smith", e.getFullName());
	}

	@Test
	void TC_LOGIN_02_invalidPassword() {
		Employee e = facade.login("E001", "JE0023");
		assertNull(e, "Expected null for invalid credentials");
	}

	@Test
	void TC_EMP_01_updatePhone() {
		Employee e = facade.login("E001", "JE001");
		assertNotNull(e);

		facade.updateMyContacts(e, "0503333444", "", "");

		assertEquals("0503333444", e.getPhoneNumber());

		HRFacade check = new HRFacade();
		check.loadAll(ftFile.toString(), ptFile.toString(), ctFile.toString(), deptFile.toString(),
				leaveFile.toString(), payrollFile.toString());
		Employee reloaded = check.login("E001", "JE001");
		assertEquals("0503333444", reloaded.getPhoneNumber(), "Phone should be updated in the file");
	}

	@Test
	void TC_EMP_02_updateEmail() {
		Employee e = facade.login("E001", "JE001");
		assertNotNull(e);

		facade.updateMyContacts(e, "", "new.mail@co.com", "");

		assertEquals("new.mail@co.com", e.getEmail());

		HRFacade check = new HRFacade();
		check.loadAll(ftFile.toString(), ptFile.toString(), ctFile.toString(), deptFile.toString(),
				leaveFile.toString(), payrollFile.toString());
		Employee reloaded = check.login("E001", "JE001");
		assertEquals("new.mail@co.com", reloaded.getEmail(), "Email should be updated in the file");
	}

	@Test
	void TC_HR_01_createFullTimeEmployee() {

		Employee e = new FullTimeEmployee("E900", "Alice Johnson", "1995-02-02", Gender.FEMALE, "alice@co.com",
				"0507777888", "2022-01-10", "Engineer", 2, EmploymentStatus.ACTIVE, "2024-05-01", "PASS900", 8000.0);
		boolean created = facade.createEmployee(e);
		assertTrue(created, "Expected creation to succeed (new ID)");

		HRFacade check = new HRFacade();
		check.loadAll(ftFile.toString(), ptFile.toString(), ctFile.toString(), deptFile.toString(),
				leaveFile.toString(), payrollFile.toString());

		assertNotNull(check.login("E900", "PASS900"), "New employee should be persisted and loggable");
	}

	@Test
	void TC_HR_02_duplicateIdRejected() {

		Employee dupe = new FullTimeEmployee("E001", "Someone Else", "1988-01-01", Gender.MALE, "dup@co.com",
				"0500000000", "2020-01-01", "Engineer", 2, EmploymentStatus.ACTIVE, "2024-01-01", "ANY", 7000.0);
		boolean created = facade.createEmployee(dupe);
		assertFalse(created, "Duplicate ID must be rejected");
	}

	@Test
	void TC_HR_03_changeDeptAndStatus() {
		Employee e = facade.login("E001", "JE001");
		assertNotNull(e);

		e.setDepartmentId(2);
		e.setStatus(EmploymentStatus.ON_LEAVE);
		facade.persistEmployee(e);

		HRFacade check = new HRFacade();
		check.loadAll(ftFile.toString(), ptFile.toString(), ctFile.toString(), deptFile.toString(),
				leaveFile.toString(), payrollFile.toString());
		Employee re = check.login("E001", "JE001");

		assertEquals(2, re.getDepartmentId());
		assertEquals(EmploymentStatus.ON_LEAVE, re.getStatus());
	}

	@Test
	void TC_HR_04_invalidDeptIdKeepsCurrent() throws IOException {

		Employee e = facade.login("E001", "JE001");
		assertNotNull(e);
		int before = e.getDepartmentId();
		assertEquals(1, before, "Seeded dept should be 1");

		String userInputDept = "UNKNOWN";
		try {
			int parsed = Integer.parseInt(userInputDept);
			e.setDepartmentId(parsed);
		} catch (NumberFormatException ex) {

		}

		facade.persistEmployee(e);

		assertEquals(before, e.getDepartmentId(), "Invalid dept should keep the current value");

		HRFacade check = new HRFacade();
		check.loadAll(ftFile.toString(), ptFile.toString(), ctFile.toString(), deptFile.toString(),
				leaveFile.toString(), payrollFile.toString());
		Employee re = check.login("E001", "JE001");
		assertNotNull(re);
		assertEquals(before, re.getDepartmentId(), "File should still have the original department id");
	}

	@Test
	void TC_PAY_01_happyPathStrategyPrints() throws Exception {

		try (PrintWriter out = new PrintWriter(new FileWriter(payrollFile.toFile(), true))) {
			out.println(String.join("\t", "PTEST", "E001", "Jul-25", "5000", "0", "0", "4500", "500", "2025-07-01"));
		}

		facade.loadAll(ftFile.toString(), ptFile.toString(), ctFile.toString(), deptFile.toString(),
				leaveFile.toString(), payrollFile.toString());

		var bout = new java.io.ByteArrayOutputStream();
		var oldOut = System.out;
		try {
			System.setOut(new java.io.PrintStream(bout, true));
			facade.processMonthlyPayrollForMonth("Jul-25");
		} finally {
			System.setOut(oldOut);
		}
		String out = bout.toString();

		assertTrue(out.contains("[Strategy] Processing payroll for Jul-25"), "Should announce month");
		boolean hasDetail = out.contains("E001") || out.contains("PTEST") || out.contains("base=")
				|| out.contains("Net:");
		assertTrue(hasDetail, "Should show at least one payroll detail line");
		assertTrue(out.contains("[Strategy] Done."), "Should finish");
		assertFalse(out.toLowerCase().contains("exception"), "No exceptions should be printed");
	}

	@Test
	void TC_PAY_02_emptyDatasetTolerance() throws IOException {

		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(ftFile))) {
			out.println(
					"employeeId\tfullName\tdateOfBirth\tgender\temail\tphoneNumber\thireDate\tjobTitle\tCOL8\tdepartmentId\tstatus\tlastPromotionDate\tpassword\tsalary");
		}
		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(ptFile))) {
			out.println(
					"employeeId\tfullName\tdateOfBirth\tgender\temail\tphoneNumber\thireDate\tjobTitle\tCOL8\tdepartmentId\tstatus\tlastPromotionDate\tpassword\thoursPerMonth\thourlyRate");
		}
		try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(ctFile))) {
			out.println(
					"employeeId\tfullName\tdateOfBirth\tgender\temail\tphoneNumber\thireDate\tjobTitle\tCOL8\tdepartmentId\tstatus\tlastPromotionDate\tpassword\tdurationMonths\toverallPayment");
		}

		HRFacade empty = new HRFacade();
		empty.loadAll(ftFile.toString(), ptFile.toString(), ctFile.toString(), deptFile.toString(),
				leaveFile.toString(), payrollFile.toString());

		java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
		java.io.PrintStream oldOut = System.out;
		try {
			System.setOut(new java.io.PrintStream(bout));
			empty.processMonthlyPayrollForMonth("Jul-01");
		} finally {
			System.setOut(oldOut);
		}
		String out = bout.toString();

		assertTrue(out.contains("[Strategy] Done."), "Should finish gracefully with no employees");

		assertFalse(out.contains(" - "), "No employee lines should be printed for empty dataset");
	}
}