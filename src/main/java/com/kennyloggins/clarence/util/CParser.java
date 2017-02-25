package com.kennyloggins.clarence.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.ParseException;
import java.util.Collections;

import org.jsoup.*;
import org.jsoup.nodes.*;


/**
 * Various text parsing utility functions.
 */
public final class CParser {
	/**
	 * Parses word counts of all text files inside the given directory.
	 * @param directory The parent directory. All text files in all subdirectories will be parsed.
	 * @param minCharCount The minimum number of characters a word needs to be stored.
	 * @param blacklist Set of words not to include when parsing.
	 * @param whitelist Set of words to include when parsing, even if filtered out by word length.
	 * @return Map containing words and their frequencies in the text files.
	 * @throws IOException If the text files cannot be read for any given reason.
	 */
    public static Map<String, Integer> parseTextFilesInDirectory(String directory, int minCharCount, Set<String> blacklist, Set<String> whitelist) throws IOException {
    	if(blacklist == null)
    		blacklist = new HashSet<>();
    	
    	if(whitelist == null)
    		whitelist = new HashSet<>();
    	
        Map<String, Integer> wordCounts = new HashMap<>();
        List<File> files = getFiles(new File(directory), ".txt");
        for(File f : files)
            parseText(f, wordCounts, minCharCount, blacklist, whitelist);
        return wordCounts;
    }
    
    /**
     * Parses word counts of all html files inside the given directory.
     * @param directory The parent directory. All html files in all subdirectories will be parsed.
     * @param htmlClass The html class that contains the desired text.
	 * @param minCharCount The minimum number of characters a word needs to be stored.
	 * @param blacklist Set of words not to include when parsing.
	 * @param whitelist Set of words to include when parsing, even if filtered out by word length.
	 * @return Map containing words and their frequencies in the html files.
     * @throws IOException If the html files cannot be read for any given reason.
     */
    public static Map<String, Integer> parseHTMLFilesInDirectory(String directory, String htmlClass, int minCharCount, Set<String> blacklist, Set<String> whitelist) throws IOException {
    	if(blacklist == null)
    		blacklist = new HashSet<>();
    	
    	if(whitelist == null)
    		whitelist = new HashSet<>();
    	
        Map<String, Integer> wordCounts = new HashMap<>();
        List<File> files = getFiles(new File(directory), ".html");
        for(File f : files)
            parseHTMLText(f, htmlClass, wordCounts, minCharCount, blacklist, whitelist);
        return wordCounts;
    }
    
    /**
     * Gets all files nested in the given directory with the given file extension.
     * @param directory The parent dictory.
     * @param fileExtension The file extension to check.
     * @return All files with the given extension that have {@code directory} as a parent directory.
     */
    private static List<File> getFiles(File directory, String fileExtension) {
        Queue<File> queue = new LinkedList<>();
        List<File> paths = new ArrayList<>();
        if(directory != null) {
            if(directory.isDirectory())
                queue.add(directory);
            else
                paths.add(directory);
        }
        while(!queue.isEmpty()) {
            File[] directoryFiles = queue.poll().listFiles();
            if(directoryFiles != null) {
                for(File child : directoryFiles) {
                    if(child.isDirectory())
                        queue.add(child);
                    else if(child.toString().endsWith(fileExtension))
                        paths.add(child);
                }
            }
        }
        return paths;
    }
    
    /**
     * Given a text file, parse its contents into a map.
     * @param file The text file to parse.
     * @param wordCounts Output variable where the word counts will be stored.
	 * @param minCharCount The minimum number of characters a word needs to be stored.
	 * @param blacklist Set of words not to include when parsing.
	 * @param whitelist Set of words to include when parsing, even if filtered out by word length.
     * @throws IOException If the text files cannot be read for any given reason.
     */
    public static void parseText(File file, Map<String, Integer> wordCounts, int minCharCount, Set<String> blacklist, Set<String> whitelist) throws IOException {
    	if(blacklist == null)
    		blacklist = new HashSet<>();
    	
    	if(whitelist == null)
    		whitelist = new HashSet<>();
    	
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while(line != null) {
                extractWordCount(line, wordCounts, minCharCount, blacklist, whitelist);
                line = br.readLine();
            }
        } 
    }
    
    /**
     * Given an html file, parse its contents into a map.
     * @param file The html file to parse.
     * @param htmlClass The html class that contains the desired text.
     * @param wordCounts Output variable where the word counts will be stored.
	 * @param minCharCount The minimum number of characters a word needs to be stored.
	 * @param blacklist Set of words not to include when parsing.
	 * @param whitelist Set of words to include when parsing, even if filtered out by word length.
     * @throws IOException If the html files cannot be read for any given reason.
     */
    public static void parseHTMLText(File file, String htmlClass, Map<String, Integer> wordCounts, int minCharCount, 
    		Set<String> blacklist, Set<String> whitelist) throws FileNotFoundException, IOException {
    	if(blacklist == null)
    		blacklist = new HashSet<>();
    	
    	if(whitelist == null)
    		whitelist = new HashSet<>();
    	
        Document doc = Jsoup.parse(file, "UTF-8");
        for(Element e : doc.getElementsByClass("main")) {
            for(Element inner : e.select("a[href]"))
                if(inner.toString().contains("health"))
                    return;
        }
        for(Element e : doc.getElementsByClass(htmlClass))
            extractWordCount(e.text(), wordCounts, minCharCount, blacklist, whitelist);
    }
    
    /**
     * Given a line of text, extract the words into the provided map. Ignores non-alphabetic characters,
     * @param line The line of text to parse.
     * @param wordCounts Output variable where the word counts will be stored.
	 * @param minCharCount The minimum number of characters a word needs to be stored.
	 * @param blacklist Set of words not to include when parsing.
	 * @param whitelist Set of words to include when parsing, even if filtered out by word length.
     */
    public static void extractWordCount(String line, Map<String, Integer> wordCounts, int minCharCount, Set<String> blacklist, Set<String> whitelist) {
    	if(blacklist == null)
    		blacklist = new HashSet<>();
    	
    	if(whitelist == null)
    		whitelist = new HashSet<>();
    	
        String[] sa = line.split("[^#a-zA-Z]");
        for(String s : sa) {
            if(blacklist.contains(s.toUpperCase())) 
                continue;
            if(!whitelist.contains(s.toUpperCase()) && s.length() < minCharCount)
                continue;
            s = s.toLowerCase();
            //"theAtEr" -> "TheAtEr"
            if(s.charAt(0) >= 'a') 
                s = (char)(s.charAt(0) - 32) + s.substring(1);

            Integer count = wordCounts.get(s);
            wordCounts.put(s, count != null ? count + 1 : 1);
        }
    }
    
    /**
     * Given a text file, returns a map containing the word count content.
     * @param file The file to parse.
	 * @param minCharCount The minimum number of characters a word needs to be stored.
	 * @param blacklist Set of words not to include when parsing.
	 * @param whitelist Set of words to include when parsing, even if filtered out by word length.
     * @return Map containing the word count in the provided file.
     * @throws IOException If the file cannot be read for any given reason.
     * @throws ParseException If the provided file does not have the appropriate content.
     */
    public static Map<String, Integer> getWordCounts(File file, int minCharCount, Set<String> blacklist, Set<String> whitelist) throws IOException, ParseException {
    	if(blacklist == null)
    		blacklist = new HashSet<>();
    	
    	if(whitelist == null)
    		whitelist = new HashSet<>();
    	
        Map<String, Integer> wordCounts = new HashMap<>();
        try(BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            while(line != null) {
                if(line.matches("^[#a-zA-Z]+ [0-9]+$")) {
                    String[] sa = line.split(" ");
                    wordCounts.put(sa[0], Integer.parseInt(sa[1]));
                }
                else
                    extractWordCount(line, wordCounts, minCharCount, blacklist, whitelist);
                    
                line = br.readLine();
            }
        }
        return wordCounts;
    }
    
    /**
     * Saves a text representation of given map ("[String] [Integer]").
     * @param wordCount The map whose content will be saved to {@code filePath}.
     * @param filePath The path to save the text output of {@code wordCounts}.
     * @throws FileNotFoundException If the given file object does not denote an existing, writable regular file and a new regular file of that name cannot be created, or if some other error occurs while opening or creating the file
     * @throws UnsupportedEncodingException If the named charset is not supported
     */
    public static void saveWordCounts(Map<String, Integer> wordCounts, String filePath) throws FileNotFoundException, UnsupportedEncodingException {
        try(PrintWriter writer = new PrintWriter(new File(filePath), "UTF-8")) {
            for(Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                writer.println(key + " " + value);
            }
        }
    }
    
    /**
     * Converts each line of the given file to an upper-cased string, returning the collection as a {@code Set}.
     * @param filePath The file to parse.
     * @return Set of upper-cased strings corresponding to the lines of the given file.
     * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some other reason cannot be opened for reading.
     * @throws IOException If an I/O error occurs
     */
    public static Set<String> fileLinesToUppercasedStringSet(File filePath) throws FileNotFoundException, IOException {
    	Set<String> wordlist = new HashSet<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                line = line.toUpperCase();
                wordlist.add(line);
            }
        }
        return wordlist;
    }
    
    /**
     * Returns a map where the values of each key are weighted by how much larger the key value
     * in {@code source} is compared to the key value in {@code target} and by the difference in
     * rank these keys lie in their perspective maps when sorted by value.
     * 
     * The goal is to return a map where keys with relatively greater frequency in {@code source} 
     * are given a higher value.
     * 
     * @param source The map whose most frequently used 
     * @param target The map to compare against {@code source}.
     * @return A map where keys with relatively greater frequency in {@code source} are given higher value.
     */
    public static Map<String, Integer> getGreatestDifferences(Map<String, Integer> source, Map<String, Integer> target) {
        Map<String, Integer> differences = new HashMap<>();
        
        List<String> listSource = new ArrayList<>(source.keySet());
        Collections.sort(listSource, (String lhs, String rhs) -> {
            int l = source.get(lhs);
            int r = source.get(rhs);
            return l > r ? -1 : l == (r) ? 0 : 1;
        });
        
        List<String> listTarget = new ArrayList<>(target.keySet());
        Collections.sort(listTarget, (String lhs, String rhs) -> {
            int l = target.get(lhs);
            int r = target.get(rhs);
            return l > r ? -1 : l == (r) ? 0 : 1;
        });
        
        //Takes the difference between the wordcounts in each map, then multiplies 
        //by the square root of the difference of their indices in the sorted lists. 
        //This is meant to represent the "greatest differences" of the maps.
        //
        //TODO: Could speed this up if necessary. Binary search looking at map values.
        for(int i = 0; i < listSource.size(); i++) {
            String keyA = listSource.get(i);
            if(target.containsKey(keyA) && source.get(keyA) > target.get(keyA)) {
                for(int j = 0; j < listTarget.size(); j++) {
                    String keyB = listTarget.get(j);
                    if(keyA.equals(keyB))
                        differences.put(keyA, (int)((Math.abs(source.get(keyA) - target.get(keyB))) * Math.sqrt(Math.abs(i - j))));
                }
            }
            else if(!target.containsKey(keyA))
                differences.put(keyA, (int)(source.get(keyA) * Math.sqrt(Math.abs(i - listTarget.size()))));
        }
        
        return differences;
    }
}
