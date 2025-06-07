import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Main {
    static StringBuilder logger = new StringBuilder();

    public static void main(String[] args) {
        //создаем список файлов .dat
        List<String> absolutePathFiles = Arrays.asList("D:/Games/savegames/game1.dat",
                "D:/Games/savegames/game2.dat",
                "D:/Games/savegames/game3.dat");

        GameProgress player1 = new GameProgress(1, 1, 1, 1.00),
                player2 = new GameProgress(10, 10, 10, 10.56),
                player3 = new GameProgress(5, 6, 7, 8.04);

        //создаем экземпляры сохранения игры
        saveGames(absolutePathFiles.get(0), player1);
        saveGames(absolutePathFiles.get(1), player2);
        saveGames(absolutePathFiles.get(2), player3);


//test        saveGames("D:/Games/savegames/game1.dat", player1);
//test        saveGames("D:/Games/not_exists/game1.dat", player1);

//создаем архив файлов сохраненных игр
        String archiveName = "D:/Games/savegames/archive.zip";
        zipFiles(archiveName, absolutePathFiles);


//удаляем файлы сохраненных игр вне архива с проверкой наличия файла в самом архиве
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(archiveName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (String file : absolutePathFiles) {
            if (searchFile(new File(file).getName(), zipFile)) {
                boolean isDeleted = new File(file).delete();
                logger.append(LocalDateTime.now() + " "
                        + (isDeleted ? file + " file deleted\n" : file + " file not deleted\n"));
            } else {
                logger.append(LocalDateTime.now() + " " + file + " file not delete, archive hasn't it\n ");
            }
        }

        //  System.out.println(logger.toString());

//добавляем логгер в файл temp.txt
        try (FileWriter writer = new FileWriter("D:/Games/temp/temp.txt", true)) {
            writer.write(logger.toString());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("logger on done");
    }

    //реализуем метод saveGames
    static void saveGames(String absolutePath, GameProgress gameProgress) {
        if (!new File(absolutePath).exists()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(absolutePath))) {

                oos.writeObject(gameProgress);
                logger.append(LocalDateTime.now() + " " + absolutePath + " file created\n");

//
            } catch (IOException ex) {
                logger.append(LocalDateTime.now() + " " + absolutePath + " file not created\n");
                System.out.println(ex.getMessage());
            }
        } else {
            logger.append(LocalDateTime.now() + " " + absolutePath + " file already exists\n");
        }
    }

    //реализуем метод zipFiles
    static void zipFiles(String pathZip, List<String> pathFiles) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathZip))) {

            for (String file : pathFiles) {
                try (FileInputStream fis = new FileInputStream(file)) {

                    ZipEntry entry = new ZipEntry(new File(file).getName());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                    logger.append(LocalDateTime.now() + " " + file + " file added in archive\n");

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    logger.append(LocalDateTime.now() + " " + pathZip + " file not added in archive\n");
                }

            }
            logger.append(LocalDateTime.now() + " " + pathZip + " file created\n");

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            logger.append(LocalDateTime.now() + " " + pathZip + " file not created\n");
// верно ли рассуждение, что если в списке pathFiles указано например 4 файла, а сохраненных saveGames есть только 3,
// то это случай выбросит исключение в блоке try-catch для FileInputStream?
        }
    }

    //проверка на наличие файла в архиве
    static boolean searchFile(String name, ZipFile zipFile) {
        try {
            ZipEntry entry = zipFile.getEntry(name);
            if (entry != null) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }
}