import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 遺伝子をテキストファイルに保存するクラス
 */
public class GeneStorage {
    private String filePath;
    private File file;

    public GeneStorage(String filePath) {
        this.filePath = filePath;
        file = new File(filePath);
        if (file.exists()) {
            throw new UnsupportedOperationException("Storage file already exists");
        }
    }

    public void save(int generation, int fitness, int[] gene) {
        StringBuilder builder = new StringBuilder();

        builder.append(generation);
        builder.append(",");
        builder.append(fitness);

        for (int value : gene){
            builder.append(",");
            builder.append(value);
        }

        builder.append(System.getProperty("line.separator"));

        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(builder.toString());
            fileWriter.close();
        } catch(IOException e) {
            System.out.println("error on writing " + filePath);
            System.out.println(e.getMessage());
        }
    }
}
