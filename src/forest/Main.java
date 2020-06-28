package forest;
import java.io.FileWriter;
import java.io.IOException;

public class Main {


    public static void main(String[] args) throws IOException {


       // FileWriter file= new FileWriter("log11.txt");
        //file.write("hello");
        //file.close();


//       for (int i = 0; i<10000;i++) {
            CForest forest = new CForest();
            //forest.initialise();
            int j = forest.initialise();
           // if (j == 0) System.out.println("2 tigra live on month " + i);
 //           if (j == 1) System.out.println("2 tigra die " + i);

 //       }

    }
}
