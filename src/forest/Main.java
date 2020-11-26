package forest;
import java.io.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.Scanner;


public class Main {


    public static void main(String[] args) throws IOException {


        System.out.print("Сhoose 1 if you want to generate 10,000 days in the forest: ");
        System.out.print("\nСhoose 2 if you want to generate 1 day in the forest: ");
        System.out.print("\nСhoose 3 if you want to generate forest from file \n  Your choice: ");
        Scanner in = new Scanner(System.in);

        int choice = in.nextInt();
        in.close();


    switch (choice){
        case 1 :
            {
                for (int i = 0; i<10000;i++) {
                CForest forest = new CForest();
                //forest.initialise();
                int j = forest.initialise();
                // if (j == 0) System.out.println("2 tigra live on month " + i);
                //           if (j == 1) System.out.println("2 tigra die " + i);
                break;

                }
            }
        case 2 :
            {
                CForest forest = new CForest();
                int j = forest.initialise();
                break;
            }
        case 3 :
            {
                CForest forest = new CForest();
                int i = forest.loadDayFromFile();



   //             FileReader fr = new FileReader("E:\\Git/Forest\\log\\log4.txt");
   //             char [] a = new char[200];   // Количество символов, которое будем считывать
   //             fr.read(a);   // Чтение содержимого в массив

   //             for(char c : a)
   //                 System.out.print(c);   // Вывод символов один за другими
   //             fr.close();

   //             readUsingScanner("log4.txt");
   //             break;
            }

    }


        //CFileWrite file1 = new CFileWrite("E:\\Git/Forest\\log4.txt");

        //List list = Files.readAllLines(file.toPath(), Charset.defaultCharset());


    }

    private static void readUsingScanner(String fileName) throws IOException {
        //Path path = Paths.get(fileName);
        //Scanner scanner = new Scanner(path);
        //scanner.useDelimiter("; ");
        //читаем построчно
        //while(scanner.hasNextLine()){
        //    String line = scanner.nextLine();
        //    System.out.print(line);
        //}
    }
}
