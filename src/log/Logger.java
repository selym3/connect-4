package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Logger {

    private static final String path = "resources/Log.txt";
    private static final File file = new File(path);
    private static final Map<String, List<String>> messages = new HashMap<String, List<String>>();

    private Logger() {
    }

    public static <T> void log(T source, boolean tagged, String... message) {
        try {
            String className = source.getClass().toString();
            String tag = tagged ? " @" + System.nanoTime() : "";

            BufferedWriter logWriter = new BufferedWriter(new FileWriter(file, false));

            // If this class has already been logged, then add the new messages to it
            if (messages.containsKey(className)) {
                // copy of old first so all messages go from oldest to newest
                List<String> copyOfOld = new LinkedList<String>();

                // Arrays.asList returns a FIXED-LENGTH List, so must iterate
                for (String m : messages.get(className)) {
                    copyOfOld.add(m);
                }

                List<String> newCopy = new LinkedList<String>();
                for (String m : message) {
                    newCopy.add(m += tag);
                }

                // combine n (new messages) with all old messages
                copyOfOld.addAll(newCopy);

                // replace in map
                messages.replace(className, copyOfOld);
            } else {
                // if the class has not been logged before, add the messages
                List<String> s = new LinkedList<String>();
                for (String m : message) {
                    s.add(m += tag);
                }

                messages.put(className, s);
            }

            messages.forEach((x, y) -> {
                // for each String (x) and List <String> (y) pair, print the messages
                try {
                    logWriter.write(x + "\n");

                    for (String s : y) {
                        logWriter.write(s + "\n");
                    }

                    logWriter.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            logWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void log(T source, String... message) {
        log(source, false, message);
    }

    public static void clear() {
        try {
            messages.clear();

            // false - clear before writing mode
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(file, false));

            logWriter.write("");

            logWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}