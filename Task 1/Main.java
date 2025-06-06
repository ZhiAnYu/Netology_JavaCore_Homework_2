import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        //создаем stringBuilder для логирования
        StringBuilder logger = new StringBuilder();

        //создаем объекты
        // src-> main->(Main.java, Utils.java),
        //       test
        // res-> drawables
        //       vectors
        //       icons
        // savegames
        // temp->(temp.txt)


        File scr = new File("D:/Games/scr");
        File res = new File("D:/Games/res");
        File savegames = new File("D:/Games/savegames");
        File temp = new File("D:/Games/temp");
        File main = new File("D:/Games/scr/main");
        File test = new File("D:/Games/scr/test");
        File drawables = new File("D:/Games/res/drawables");
        File vectors = new File("D:/Games/res/vectors");
        File icons = new File("D:/Games/res/icons");
        File mainJava = new File(main, "Main.java");
        File utilsJava = new File(main, "Utils.java");
        File tempTXT = new File(temp, "temp.txt");

        List<File> directory = Arrays.asList(scr, res, savegames, temp, main, test, drawables, vectors, icons);
        List<File> files = Arrays.asList(mainJava, utilsJava, tempTXT);

        for (File file : directory) {
            logger.append(LocalDateTime.now() + " ");
            logger.append(file.mkdir() ? file.getAbsolutePath() + " directory created \n"
                    : file.getAbsolutePath() + " directory not created \n");
            System.out.println(file.getName() + " on done");
        }

        for (File file : files) {
            logger.append(LocalDateTime.now() + " ");
            try {
                logger.append(file.createNewFile() ? file.getAbsolutePath() + " file created \n"
                        : file.getAbsolutePath() + " file not created \n");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println(file.getName() + " on done");
        }

        //записать logger в temp.txt
        try (FileWriter writer = new FileWriter("D:/Games/temp/temp.txt")) {
            writer.write(logger.toString());
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("logger on done");
    }

}
