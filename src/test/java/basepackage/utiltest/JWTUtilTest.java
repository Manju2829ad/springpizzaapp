package basepackage.utiltest;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class JWTUtilTest {
    public static void main(String[] args) {
        String privateKeyPath = System.getenv("PRIVATE_KEY_PATH");
        String publicKeyPath = System.getenv("PUBLIC_KEY_PATH");

        if (privateKeyPath == null || publicKeyPath == null) {
            System.out.println("Private or Public key path is not set!");
            return;
        }

        System.out.println("Private Key Path: ");
        System.out.println(privateKeyPath);

        System.out.println("Public Key Path: ");
        System.out.println(publicKeyPath);

        // Optionally, load the keys from the paths to ensure they exist and save them to new files
        try {
            byte[] privateKeyBytes = Files.readAllBytes(Paths.get(privateKeyPath));
            byte[] publicKeyBytes = Files.readAllBytes(Paths.get(publicKeyPath));

            // Write the loaded key contents to test files
            Files.write(Paths.get("privateKeyTest.pem"), privateKeyBytes);
            Files.write(Paths.get("publicKeyTest.pem"), publicKeyBytes);

            System.out.println("Keys have been written to test files.");
        } catch (IOException e) {
            System.err.println("Error loading or writing keys: " + e.getMessage());
        }
    }
}
