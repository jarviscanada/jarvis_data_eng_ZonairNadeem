package ca.jrvs.apps.grep;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class JavaGrepImp implements JavaGrep{
    private static final Logger logger = LoggerFactory.getLogger(JavaGrepImp.class);

    private String regex;
    private String rootPath;
    private String outFile;

    @Override
    public String getRegex() {
        return regex;
    }

    @Override
    public void setRegex(String regex) {
        logger.info("Setting the Regex: {}", regex);
        this.regex = regex;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        logger.info("Setting Root: {}", rootPath);
        this.rootPath = rootPath;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        logger.info("Setting Out File: {}", outFile);
        this.outFile = outFile;
    }

    @Override
    public List<File> listFiles(String rootDir) {
        List<File> list = new ArrayList<File>();
        //getting file object
        File dr = new File(rootDir);

        //list of all files and directories in root directory
        File[] listFiles = dr.listFiles();

        for(File f : listFiles){
            if(f.isFile()){
                list.add(f);
            }
        }

        return list;
    }

    @Override
    public void process() throws IOException {
        logger.info("STARTING PROCESS");

        ArrayList<String> matchedLines = new ArrayList<>();
        for (File file : listFiles(rootPath)) {
            for (String line : readLines(file)) {
                if (containsPattern(line)){
                    matchedLines.add(line);
                }
            }
        }

        writeToFile(matchedLines);
        logger.info("Processing complete");

    }


    @Override
    public List<String> readLines(File inputFile) {
        BufferedReader reader;
        List<String> linesList = new ArrayList<>();
        try{
            reader = new BufferedReader(new FileReader(inputFile));
            String line = reader.readLine();
            while (line!=null){
                linesList.add(line);
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            logger.error("FILE IS NOT FOUND", e);
        } catch (IOException e){
            logger.error("File could not be read");
        }
        return linesList;



    }

    @Override
    public boolean containsPattern(String line) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        return matcher.find();
    }

    @Override
    public void writeToFile(List<String> Lines) throws IOException {
        logger.info("Start of Write to file");
        BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));

        for(String line : Lines){
            bw.write(line + "\n");
        }
        bw.close();
        logger.info("End of File Writing!");
    }

    public static void main(String[] args) {
        logger.info("Starting MAIN");
        if(args.length!=3){
            throw new IllegalArgumentException("USAGE: JAVAGREP regex rootPath outFile");
        }
        JavaGrepImp javaGrepImp = new JavaGrepImp();
        javaGrepImp.setRegex(args[0]);
        javaGrepImp.setRootPath(args[1]);
        javaGrepImp.setOutFile(args[2]);


        try {
            javaGrepImp.process();
        } catch (Exception e) {
            logger.error("Error: unable to process", e);
        }

    }


}
