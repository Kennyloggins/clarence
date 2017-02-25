package com.kennyloggins.clarence.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Given a list of URLs, extracts related URLs
 * 
 * @author Bill Eberlein
 */
public final class UrlExtractor {
    // Regex for validating if a URL is worth processing.
    private Pattern validUrlRegex;
    
    // Maximum time for threads to wait before timing out.
    private int timeout;
    
    // Maximum number of URLs to process. If 0, threads will process until timeout.
    private int maxUrlsToProcess = 0;
    
    // Keep track of which URLs have been visited
    private ConcurrentHashMap<String, Boolean> masterUrlMap;
    
    // Synchronized queue of URLs that have not yet been processed by UrlExtractWorker
    private final ConcurrentLinkedQueue<String> urlsToProcess;
    
    // Number of threads
    private List<Thread> extractorThreads;
    
    // Runnable instance for multithreading
    private UrlExtractWorker ue;
    
    /**
     * @param seedUrls The initial set of URLs to process. UrlExtractor will then branch out and process any related URLs within this set.
     * @param htmlClasses Set of HTML classes that might contain related URLs.
     * @param validUrlRegex Regex for validating if a URL is worth processing.
     * @param numThreads Number of threads to used when processing URLs.
     * @param maxUrlsToProcess Maximum number of URLs to process. If 0 or lower, threads will process until timeout.
     * @param timeout Length of time before threads timeout. Minimum of 2000 milliseconds. Defaults to 5000 milliseconds.
     */
    UrlExtractor(String[] seedUrls, String validUrlRegex, int numThreads, int maxUrlsToProcess, int timeout) {
        this.validUrlRegex = Pattern.compile(validUrlRegex);
        
        //Initialize 
        ue = new UrlExtractWorker();
        extractorThreads = new ArrayList<>();
        urlsToProcess = new ConcurrentLinkedQueue<>();
        masterUrlMap = new ConcurrentHashMap<>();
        
        if(maxUrlsToProcess > 0)
            this.maxUrlsToProcess = maxUrlsToProcess;
        
        this.timeout = timeout;
        
        //Add the first set of URLs to the processing queue
        for(String u : seedUrls) {
            addUrlToQueue(u);
        }
        
        //Create the threads
        for(int i = 0; i < numThreads; i++) { 
            Thread t = new Thread(ue);
            extractorThreads.add(t);
        }
    }

    UrlExtractor(String[] seedUrls, String validUrlRegex, int numThreads) {
        this(seedUrls, validUrlRegex, numThreads, 0, 5000);
    }
    

    UrlExtractor(String[] seedUrls, String validUrlRegex, int numThreads, int maxUrlsToProcess) {
        this(seedUrls, validUrlRegex, numThreads, maxUrlsToProcess, 5000);
    }

    /**
     * Runnable instance if multiple threads are being used
     */
    private class UrlExtractWorker implements Runnable {
        @Override
        public void run() {
            try {
                while(maxUrlsToProcess == 0 || masterUrlMap.size() <= maxUrlsToProcess) {
                    synchronized(urlsToProcess) {
                        while(urlsToProcess.isEmpty())
                            urlsToProcess.wait(timeout);
                        
                        if(!urlsToProcess.isEmpty()) {
                            String url = urlsToProcess.poll();
                            try {
                                findLinks(url, 0, 1);
                            } catch(IOException e) {
                                masterUrlMap.put(url, true);
                            }
                        }
                    }
                }
            } catch (InterruptedException ix) {
                System.out.println(ix.getClass().getName() + " " + ix.getMessage());
            }
        }
    }
    
    /**
     * Finds all linked urls in the given url, adding valid links (determined by regex used when constructing the {@code UrlExtractor}) to the processing queue.
     * @param url - the source url to scan.
     * @param currentDepth - the current depth of calls to {@code findLinks}. Will be called recursively up to {@code maxDepth}.
     * @param maxDepth - the maximum depth this function should be recursively called.
     * @throws IOException - if an I/O error occurs
     */
    private void findLinks(String url, int currentDepth, int maxDepth) throws IOException {
        Document doc = Jsoup.parse(new URL(url), 2000); //Request and read HTML
        Elements links = doc.select("a[href]");
        for(Element link : links) {
            String u = link.attr("abs:href");
            
            if(!masterUrlMap.containsKey(u)) {
                masterUrlMap.put(u, true);
                if(currentDepth < maxDepth)
                    findLinks(u, currentDepth+1, maxDepth);
                
                Matcher m = validUrlRegex.matcher(u);
                
                if(m.find()) {
                    //Add next link to the processing queue
                    addUrlToQueue(u);
                }
            }
        }
        
        Matcher m = validUrlRegex.matcher(url);
        
        if(m.find()) {
            String webSite = m.group(1);
            String date = m.group(2);
            String title = m.group(3);
            if(date.matches("[0-9]{4}/[0-9]{2}/[0-9]{2}")) {
                new File(webSite + "/" + date + "/").mkdirs();
                writeDocumentToFile(doc, webSite + "/" + date + "/" + title + ".html");
            }
        }
    }
    
    /**
     * 
     * @param doc - HTML document to locally save
     * @param path - the local path to save the HTML
     */
    private void writeDocumentToFile(Document doc, String path) {
        try{
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.println(doc.html());
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getClass().getName() + " " + e.getMessage());
        }
    }
    
    /**
     * Adds URL to processing queue and alerts
     * any waiting threads.
     * 
     * @param url - url to queue
     */
    public void addUrlToQueue(String url) {
        synchronized(urlsToProcess) {
            urlsToProcess.offer(url);
            urlsToProcess.notifyAll();
        }
    }
    
    /**
     * @return The number of URLs that have been 
     * processed so far.
     */
    public int size() {
        return masterUrlMap.size();
    }
    
    /**
     * Starts all threads.
     */
    public void start() {
        for(int i = 0; i < extractorThreads.size(); i++)
            extractorThreads.get(i).start();
    }
    
    /**    
     * @return Whether or not any threads are still running.
     */
    public boolean running() {
        for(int i = 0; i < extractorThreads.size(); i++) {
            if(extractorThreads.get(i).isAlive())
                return true;
        }
        return false;
    }
    
    /**
     * @return Exact number of active threads. 
     */
    public int activeThreads() {
        int numActiveThreads = 0;
        for(int i = 0; i < extractorThreads.size(); i++)
            numActiveThreads += extractorThreads.get(i).isAlive() ? 1 : 0;
        return numActiveThreads;
    }
}
