import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
/**
 * Mostly simple, but we want file i/o with encryption to store codes, inaccessible to anyone/anything else. We want
 * the code to be persistent; initialized with a default if no file found, and pulled from that access file if it's
 * found on load.
 */
public class Database {
    private int userCode;
    private int adminCode;

    public Database() throws Exception {
        int encrypt = 243;
        File file = new File("src/Codes.txt");
        Scanner sc = new Scanner(file);
        if(sc.hasNextLine()) {
            //System.out.println(sc.nextLine());
            userCode = Integer.parseInt(sc.nextLine()) / encrypt;
        }
        if(sc.hasNextLine()) {
            adminCode = Integer.parseInt(sc.nextLine()) / encrypt;
        }
        else {
            userCode = 1111;
            adminCode = 2222;
            FileWriter writer = new FileWriter(file,false);
            int enCode = userCode * encrypt;
            writer.write(Integer.toString(enCode));
            writer.write("\n");
            enCode = adminCode * encrypt;
            writer.write(Integer.toString(enCode));
            writer.close();

        }
    }
    /**
     * Checks the community code entered by the CommGateDriver.
     * @param inputCode 4 digit integer code.
     * @return True if input equals adminCode. False otherwise.
     */
    public boolean checkCode(int inputCode){
        return (userCode == inputCode || adminCode == inputCode);
    }

    /**
     * Checks the community code entered by the CommGateDriver.
     * @param inputCode 4 digit integer code.
     * @return True if input equals adminCode. False otherwise.
     */
    public boolean checkAdminCode(int inputCode){
        return adminCode == inputCode;
    }

    /**
     * Updates the Database with a new Community Gate Code
     * @param inputCode a 4 digit integer code all other values will fail.
     * @return true if successful, false if unsuccessful.
     */
    public boolean updateUserCode(int inputCode) throws Exception {
        if(inputCode >= 1000 && inputCode <=  9999){
            userCode = inputCode;
            int encrypt = 243;
            File file = new File("src/Codes.txt");
            FileWriter writer = new FileWriter(file,false);
            int enCode = userCode * encrypt;
            writer.write(Integer.toString(enCode));
            writer.write("\n");
            enCode = adminCode * encrypt;
            writer.write(Integer.toString(enCode));
            writer.close();
            return true;
        }
        return false;
    }

    /**
     * Updates the Database with a new Admin Community Gate Code.
     * @param inputCode a 4 digit integer code all other values will fail.
     * @return true if successful, false if unsuccessful.
     */
    public boolean updateAdminCode(int inputCode) throws Exception {
        if(inputCode >= 1000 && inputCode <=  9999){
            adminCode = inputCode;
            int encrypt = 243;
            File file = new File("src/Codes.txt");
            FileWriter writer = new FileWriter(file,false);
            int enCode = userCode * encrypt;
            writer.write(Integer.toString(enCode));
            writer.write("\n");
            enCode = adminCode * encrypt;
            writer.write(Integer.toString(enCode));
            writer.close();
            return true;
        }
        return false;
    }

}
