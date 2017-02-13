package survivalgame.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public abstract class JsonConfiguration {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T load(File file, Class<T> expectedClass) {
        try {
            if (!file.isFile()) {
                return expectedClass.newInstance();
            }

            return gson.fromJson(new FileReader(file), expectedClass);
        } catch (IOException | ReflectiveOperationException ex) {
            return null;
        }
    }

    public boolean save(File file) {
        try (FileWriter fw = new FileWriter(file)) {
            fw.write(gson.toJson(this));
            fw.flush();

            return true;
        } catch (IOException ex) {
            return false;
        }
    }
}
