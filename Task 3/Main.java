import java.io.*;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    static StringBuilder logger = new StringBuilder();

    public static void main(String[] args) {

        String archiveName = "D:/Games/savegames/archive.zip";
        String unarchivedDirectory = "D:/Games/savegames/";

//распаковка архива
        openZip(archiveName, unarchivedDirectory);

//десериализация

        GameProgress player1 = openProgress(unarchivedDirectory + "game1.dat");
        GameProgress player2 = openProgress(unarchivedDirectory + "game2.dat");

//запись лога в temp.txt
        try (FileWriter writer = new FileWriter("D:/Games/temp/temp.txt", true)) {
            writer.write(logger.toString());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("logger on done");
    

//вывод игры нв консоль
        System.out.println(player1);
        System.out.println(player2);
    }

//реализация метода openProgress

    static GameProgress openProgress(String absolutePathFile) {
        GameProgress gameProgress = null;
        //откроем входной поток для чтения файла
        try (FileInputStream fis = new FileInputStream(absolutePathFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            gameProgress = (GameProgress) ois.readObject();
            logger.append(LocalDateTime.now() + " " + absolutePathFile + " progress game is open\n");
            return gameProgress;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            logger.append(LocalDateTime.now() + " progress game not open\n");
            return null;
        }
    }


//реализация метода openZip

    static void openZip(String archiveName, String unarchivedDirectory) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(archiveName))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(unarchivedDirectory + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
                logger.append(LocalDateTime.now() + " " + name + " file is unzipped in " + unarchivedDirectory + "\n");
            }
            logger.append(LocalDateTime.now() + " " + archiveName + " is unarchived\n");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            logger.append(LocalDateTime.now() + " " + archiveName + " archive isn't unarchived\n");
        }
    }
}
