package pl.saletrak.wypokplus.resource;

import android.content.Context;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileData {

    Context context;
    String filename;
    File file;

    public FileData(Context context, String filename) {
        this.context = context;
        this.filename = filename;
        this.file = context.getFileStreamPath(filename);
    }

    public boolean fileExists() {
        if(file.exists()) return true;
        else return false;
    }

    public String getContent() {
        String content = null;
        if(this.fileExists()) {
            try {
                FileInputStream fis = context.openFileInput(filename);
                StringBuffer stringBuffer = new StringBuffer();
                DataInputStream dataIO = new DataInputStream(fis);
                String strLine = null;

                while ((strLine = dataIO.readLine()) != null) {
                    stringBuffer.append(strLine + "\n");
                }

                dataIO.close();
                fis.close();

                content = stringBuffer.toString();
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        else {
            this.saveFile("");
            return "";
        }
        return content;
    }

    public void saveFile(String content) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(filename, context.MODE_PRIVATE);
            fos.write(content.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile() {
        context.deleteFile(filename);
    }

    /*
    LISTA PLIKOW W KATALOGU
    File file2 = new File(getFilesDir().getPath());
    String[] files = file2.list();
    for(int i=0; i<files.length; i++) {
        Log.d("dbg_file_list", files[i]);
    }*/

}
