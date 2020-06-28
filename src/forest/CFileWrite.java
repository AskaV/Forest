package forest;
//import java.io.File;
import java.io.*;

public class CFileWrite {

    public CFileWrite() throws IOException {
        //newFile("textfile.txt");
    }

    public void WriteFile(String tfName, String text) throws IOException {
        FileWriter writer = new FileWriter(tfName, true);
        writer.write(text);
        //writer.write("\n");
        writer.close();
    }

    public void newFile(String tfName) throws IOException {//throws Exception

        File f1 = new File("E:\\Git\\Forest\\",tfName);

        if (f1.exists()) {
            f1.delete();
        }

        String path1 = f1.getPath();
        System.out.println(path1);
        f1.createNewFile();

        //f1.createNewFile();

        //FileWriter nFile = new FileWriter( "text.txt" );
        //E:\Git\Forest
        //nFile.close();//закрываем поток для записи
    }

}
