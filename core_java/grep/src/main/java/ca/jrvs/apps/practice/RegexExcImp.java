package grep.src.main.java.ca.jrvs.apps.practice;
import java.util.regex.*;

public class RegexExcImp implements RegexExc{


    @Override
    public boolean matchJpeg(String filename) {

        Pattern p = Pattern.compile("\\w*.jpeg$||\\w*.jpg");
        Matcher m = p.matcher(filename);
        boolean b = m.matches();
        System.out.println(b);
        return b;
    }

    @Override
    public boolean matchIp(String ip) {
        Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher m = p.matcher(ip);
        boolean b = m.matches();
        System.out.println(b);
        return b;
    }

    @Override
    public boolean isEmptyLine(String line) {
        Pattern p = Pattern.compile("\\s*");
        Matcher m = p.matcher(line);
        boolean b = m.matches();
        System.out.println(b);
        return b;
    }

//    public static void main(String args[]){
//        RegexExcImp r = new RegexExcImp();
////        r.matchJpeg("gasd_fFFdf.jpeg");
////        r.matchIp("72.0.44.02");
////        r.isEmptyLine("     \n");
    //}
}
