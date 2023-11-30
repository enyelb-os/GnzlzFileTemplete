package tools.gnzlz.template.file;

import tools.gnzlz.system.ansi.Color;
import tools.gnzlz.system.io.SystemIO;
import tools.gnzlz.template.file.data.ObjectFile;

import java.io.*;
import java.nio.file.Files;

public class File {

    /**
     * file
     */
    private static final File file = new File();

    /**
     * read
     * @param url url
     * @param internal internal
     */
    public static ObjectFile read(String url, boolean internal){
        return file.reader(url, internal);
    }

    /**
     * read
     * @param url url
     */

    public static ObjectFile read(String url){
        return file.reader(url, true);
    }

    /**
     * reader
     * @param url url
     * @param internal internal
     */
    private ObjectFile reader(String url, boolean internal) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        try {
            InputStream is = internal ? getClass().getResourceAsStream(url) : new FileInputStream(new java.io.File(url));
            assert is != null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            return new ObjectFile(false, url + " fail load file");
        }
        return new ObjectFile(true, stringBuilder.toString());
    }

    /**
     * create
     * @param url url
     * @param name name
     * @param content c
     * @param replace r
     */
    public static ObjectFile create(String url, String name, String content, boolean replace) {
        return File.createFile(url, name, content, replace);
    }

    /**
     * create
     * @param url url
     * @param name name
     * @param ext e
     * @param content c
     * @param replace r
     */
    public static ObjectFile create(String url, String name, String ext, String content, boolean replace) {
        return File.createFile(url, name + "." + ext, content, replace);
    }

    /**
     * create
     * @param url url
     * @param name name
     * @param content c
     * @param replace r
     */
    private static ObjectFile createFile(String url, String name, String content, boolean replace) {
        try {
            java.io.File folder = new java.io.File(url);
            if(!folder.exists()){
                boolean mkdirs = folder.mkdirs();
            }
            java.io.File file = new java.io.File(folder.toPath() + "/" + name );
            if(!file.exists() || replace) {
                if(!file.exists()){
                    Files.createFile(file.toPath());
                }
                FileWriter fileWriter = new FileWriter(file.toString());
                fileWriter.write(content);
                fileWriter.close();
                SystemIO.OUT.println(Color.GREEN.print("file crated: ") + Color.PURPLE.print(file));
                return new ObjectFile(true, "");
            }
        } catch (IOException e) {
            return new ObjectFile(false, url + " fail create file");
        }
        return new ObjectFile(false, "");
    }
}
