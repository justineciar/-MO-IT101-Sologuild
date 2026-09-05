/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package alfie.util;

/*
*   Add-ons only 
*   Use for password hash reader
*/

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordAuthenticator {

// Using a FilePathManager(Java Class in util folder) no need for hard coded file path
    private final String adminFilePath = FilePathManager.getInstance().getAdminFilePath();

    public boolean authenticate(String username, String password) {
    String inputHash = hashPassword(password);

    try (BufferedReader reader = new BufferedReader(new FileReader(adminFilePath))) {
        String line;
        boolean isFirst = true;

        while ((line = reader.readLine()) != null) {
            if (isFirst) {
                isFirst = false; // Skip header
                continue;
            }

            String[] parts = line.split(",");
            if (parts.length == 2 && parts[0].trim().equals(username.trim())) {
                // Debugging output in console; use for debuging the hash password.
                System.out.println("Input username: " + username);
                System.out.println("Input hash: " + inputHash);
                System.out.println("Stored username: " + parts[0]);
                System.out.println("Stored hash: " + parts[1]);

                return parts[1].trim().equalsIgnoreCase(inputHash);
            }
        }
    } catch (IOException e) {
        System.err.println("Error reading admin file: " + e.getMessage());
    }

    return false;
}


    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found.");
        }
    }
}
