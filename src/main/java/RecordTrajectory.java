import java.io.*;
import java.util.*;
import edu.wpi.first.networktables.*;

public class RecordTrajectory {
    public static void main(String[] args) throws IOException {

        // get file name
        System.out.println("Enter file name for trajectory (avoid using spaces).");
        Scanner sc = new Scanner(System.in);
        String fileName = sc.nextLine();

        // set up io, write first line to file
        File file = new File(fileName + ".csv");
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("lVoltage,rVoltage,lVel,rVel");

        // set up networktables
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("datatable");
        inst.startClientTeam(4188);
        inst.startDSClient();

        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                System.out.println("tt");
            }
        };

        // start recording
        t.scheduleAtFixedRate(tt, 0, 20);
        if(sc.nextLine().equals("q")) {
            t.cancel();
            tt.cancel();
        }

        // clean up
        System.out.println("done");

    }


}