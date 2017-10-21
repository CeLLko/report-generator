/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.sbapr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oleg Ryaboy, based on work by Miguel Enriquez 
 */
public class WindowsRegistry {

    /**
     * 
     * @param location path in the registry
     * @param key registry key
     * @return registry value or null if not found
     */
    public static final String readRegistry(String location, String key){
        try {
            // Run reg query, then read output with StreamReader (internal class)
            Process process = Runtime.getRuntime().exec("reg query " + 
                    '"'+ location + "\" /v " + key);

            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();
            output = output.split("\\r\\n")[2];
            Pattern pattern = Pattern.compile("\\s+\\S+\\s+\\S+\\s+(.*)");
            Matcher matcher = pattern.matcher(output);

            if (matcher.matches()) {
                return matcher.group(1);
            }else
                return null;
        }
        catch (Exception e) {
            return null;
        }

    }

    static class StreamReader extends Thread {
        private InputStream is;
        private StringWriter sw= new StringWriter();

        public StreamReader(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e) { 
        }
        }

        public String getResult() {
            return sw.toString();
        }
    }
}