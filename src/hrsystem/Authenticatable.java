package hrsystem;

public interface Authenticatable {
	String getLoginId();

	boolean checkPassword(String raw);
}