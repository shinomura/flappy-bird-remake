import javax.swing.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Nikita on 3/2/14.
 */
public class gameDataExporter{
    final String hscore = "#HIGHSCORE ";
    final String ulktstore = "#STOREUNLOCK ";
    final String activebuy = "#ACTIVEBUY ";
    final String activegm = "#ACTIVEGMDE ";
    final String chosenbrdpth = "#BIRDPATH ";
    final String coinss = "#COINS ";
    public void composeExport(int highscore,ArrayList<Integer> storeunlocks,ArrayList<Integer> activebuys,int activegamemode,String birdpath,int coins){
        ArrayList<String> file = new ArrayList<String>();
        file.add(hscore+Integer.toOctalString(highscore));
        for (int i : storeunlocks){
            file.add(ulktstore+Integer.toOctalString(i));
        }
        for (int i : activebuys){
            file.add(activebuy+Integer.toOctalString(i));
        }
        file.add(activegm+Integer.toOctalString(activegamemode));
        file.add(chosenbrdpth+birdpath);
        file.add(coinss+Integer.toOctalString(coins));
        writeToFile(file);
    }
    public void writeToFile(ArrayList<String> file){
        PrintWriter writer = null;
        try {
            String dataFolder = System.getenv("APPDATA");
            writer = new PrintWriter(dataFolder+"\\flappybird\\gamefo.txt");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,e.getMessage());
            e.printStackTrace();
        }
        for (String i : file){
            writer.println(i);
        }
        writer.close();
    }
}