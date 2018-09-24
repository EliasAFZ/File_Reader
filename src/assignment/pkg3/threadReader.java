package assignment.pkg3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Assignment #3
 *
 * @author Elias Afzalzada
 */
public class threadReader extends Thread {

    //Variables and handles
    private int numOfFiles = 0;
    private int numOfWords = 0;
    private int numOfLetters = 0;
    private String filePath = "";
    private String fileText = "";
    private String fileName = "";
    private List<File> fileList;
    private File file;
    private Scanner scan;
    private Thread t;
    private ArrayList<String> list;
    private CompletableFuture<List<String>> future;

    //sets up the file list to be parsed
    public void setlist(List<File> _fileList) {
        numOfFiles = _fileList.size();
        fileList = _fileList;
    }

    //Sets up list of files and CompletableFuture
    public CompletableFuture<List<String>> fileSetup(int _i) {
        try {
            fileName = fileList.get(_i).getName();
            this.filePath = fileList.get(_i).toString();
            file = new File(filePath);
            scan = new Scanner(file);
            future = new CompletableFuture<>();
            return future;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(threadReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    //Starts a thread
    public void start() {
        t = new Thread(this);
        t.start();
    }

    //Runs the algorithm to count words/letters
    @Override
    public void run() {
        fileText = "";
        while (scan.hasNext()) {
            fileText += scan.nextLine();
        }
        String replaceAll = fileText.replaceAll("\\s+", "");
        numOfLetters += replaceAll.length();
        numOfWords += fileText.split(" ").length;
        list = new ArrayList<>();
        list.add(fileText);
        list.add(" " + numOfLetters);
        list.add(" " + numOfWords);
        numOfLetters = 0;
        numOfWords = 0;
        future.complete(list);
    }

    //Getters and Setters
    public String getNumOfWords() {
        return Integer.toString(numOfWords);
    }

    public String getNumOfLetters() {
        return Integer.toString(numOfLetters);
    }

    public int getNumOfWindows() {
        return numOfFiles;
    }

    public String getFileText() {
        return fileText;
    }

    public String getFileName() {
        return fileName;
    }

}
