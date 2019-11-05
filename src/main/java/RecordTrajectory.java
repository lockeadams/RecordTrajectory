import java.io.*;
import java.util.*;
import edu.wpi.first.networktables.*;

public class RecordTrajectory {

    public static void main(String[] args) throws IOException {

        // get instance, only continue program if connected
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        if(inst.isConnected() && inst.isValid()) {

            // set up tables and start client
            NetworkTable table = inst.getTable("datatable");
            inst.setUpdateRate(0.02);
            inst.startClientTeam(4188);
            inst.startDSClient();

            // get entries
            NetworkTableEntry lVoltageEntry = table.getEntry("L Voltage");
            NetworkTableEntry rVoltageEntry = table.getEntry("R Voltage");
            NetworkTableEntry lVelEntry = table.getEntry("L Velocity");
            NetworkTableEntry rVelEntry = table.getEntry("R Velocity");

            // get file name
            System.out.println("Enter file name for trajectory (avoid using spaces).");
            Scanner sc = new Scanner(System.in);
            String fileName = sc.nextLine();

            // set up io, write first line to file
            File file = new File(fileName + ".csv");
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("lVoltage,rVoltage,lVel,rVel\n");

            // set up task to write values at consistent interval
            Timer t = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {

                    // get vals from networktables
                    double lVoltage = lVoltageEntry.getDouble(0.0);
                    double rVoltage = rVoltageEntry.getDouble(0.0);
                    double lVel = lVelEntry.getDouble(0.0);
                    double rVel = rVelEntry.getDouble(0.0);

                    // format data output
                    String line = lVoltage + "," + rVoltage + "," + lVel + "," + rVel + "\n";

                    // write to file
                    try {
                        bw.write(line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            };

            // start recording
            t.scheduleAtFixedRate(tt, 0, 20);

            // stop recording on keypress "q"
            if(sc.nextLine().equals("q")) {
                t.cancel();
                t.purge();
                bw.close();
                sc.close();
            }

        } else {
            System.out.println("Couldn't connect to NetworkTable!");
        }

    }

}
