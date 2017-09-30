import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Test {
	public static void main(String[] args) throws IOException {
		HashSet<String> reading = new HashSet<>();
		try (BufferedReader br = new BufferedReader(new FileReader("/Users/bala_in/Documents/Freelance/1CompateFiles/ejubi/mod-rewrite.php"))) {
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
				reading.add(line.trim());
			}
		}
		System.out.println(reading);
	}
}
