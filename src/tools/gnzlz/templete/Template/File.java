package tools.gnzlz.templete.Template;

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
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    /**********************************
     * create
     **********************************/

    public static boolean create(String url, String name, String ext, String content, boolean replace) {
        try {
            java.io.File folder = new java.io.File(url);
            if(!folder.exists()){
                folder.mkdirs();
            }
            java.io.File file = new java.io.File(folder.toPath() + "/" + name + "." + ext);
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
            e.printStackTrace();
        }
        return false;
    }
}
