import javax.swing.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoreFile {
    private static final String FILE_PATH = "game_data.txt";
    public static int getHighestScore(JFrame parentWindow) {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            JOptionPane.showMessageDialog(parentWindow, "No score found. You had better start playing!", "Highest Score", JOptionPane.INFORMATION_MESSAGE);
            return 0;
        }else{
            try {
                // Read encrypted content
                String encryptedData = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
                String decryptedData = EncryptionUtils.decrypt(encryptedData);

                // Verify the decrypted data format
                String[] parts = decryptedData.split(",");
                if (parts.length != 3) {
                    throw new IllegalArgumentException("Data format is invalid");
                }

                String date = parts[0];
                String time = parts[1];
                int score = Integer.parseInt(parts[2]);

                // Display the highest score
                //JOptionPane.showMessageDialog(parentWindow, "Highest Score: " + score + "\nDate: " + date + "\nTime: " + time, "Highest Score", JOptionPane.INFORMATION_MESSAGE);
                return score;

            } catch (Exception e) {
                e.printStackTrace();
                // Show a message that the data is invalid and delete the file
                JOptionPane.showMessageDialog(parentWindow, "Couldn't find valid game data. Deleting corrupted file.", "Error", JOptionPane.ERROR_MESSAGE);

                // Delete the invalid file
                if (file.exists()) {
                    file.delete();  // Delete the corrupted file
                }

                return 0;
            }
        }
    }

    // Method to save the highest score with encryption
    public static void saveHighestScore(int score) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
        String date = sdf.format(new Date());
        String time = stf.format(new Date());

        String data = date + "," + time + "," + score;

        try {
            // Encrypt the data
            String encryptedData = EncryptionUtils.encrypt(data);

            // Write encrypted data to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
            writer.write(encryptedData);
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
