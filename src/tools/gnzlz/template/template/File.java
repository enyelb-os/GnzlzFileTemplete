package tools.gnzlz.template.template;

import java.io.*;
import java.nio.file.Files;

public class File {

    /**********************************
     * Vars
     **********************************/

    private static final File file = new File();

    /**********************************
     * read
     **********************************/

    public static String read(String url, boolean internal){
        return file.reader(url, internal);
    }

    /**********************************
     * read
     **********************************/

    public static String read(String url){
        return file.reader(url, true);
    }

    /**********************************
     * read
     **********************************/

    private String reader(String url, boolean internal) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            InputStream is = internal ? getClass().getResourceAsStream(url) : new FileInputStream(new java.io.File(url));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new RuntimeException(url + " fail load file");
        }
        return stringBuilder.toString();
    }

    /**********************************
     * create
     **********************************/

    public static boolean create(String url, String name, String ext, String content, boolean replace) {
        return create(url, name + "." +ext, content, replace);
    }

    /**********************************
     * create
     **********************************/

    public static boolean create(String url, String name, String content, boolean replace) {
        try {
            java.io.File folder = new java.io.File(url);
            if(!folder.exists()){
                folder.mkdirs();
            }
            java.io.File file = new java.io.File(folder.toPath() + "/" + name );
            if(!file.exists() || replace) {
                if(!file.exists()){
                    Files.createFile(file.toPath());
                }
                FileWriter fileWriter = new FileWriter(file.toString());
                fileWriter.write(content);
                fileWriter.close();
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(url + " fail create file");
        }
        return false;
    }
}
