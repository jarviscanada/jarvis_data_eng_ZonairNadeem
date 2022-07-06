package grep.src.main.java.ca.jrvs.apps.grep;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
        this.regex = regex;
    }

    @Override
    public String getRootPath() {
        return rootPath;
    }

    @Override
    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    @Override
    public String getOutFile() {
        return outFile;
    }

    @Override
    public void setOutFile(String outFile) {
        this.outFile = outFile;
    }


    @Override
    public void process() throws IOException {
        logger.info("STARTING PROCESS");

    }

    @Override
    public List<File> ListFiles(String rootDir) {
        return null;
    }

    @Override
    public List<String> readLines(File inputFile) {
        return null;
    }

    @Override
    public boolean containsPattern(String line) {
        return false;
    }

    @Override
    public void writeToFile(List<String> Lines) throws IOException {

    }

    public static void main(String[] args) {

    }


}
