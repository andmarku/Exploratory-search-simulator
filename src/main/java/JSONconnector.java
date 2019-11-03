package com.findwise.connect.base;

import com.findwise.connect.api.TraverseConnector;
import com.findwise.connect.api.event.ContentDocument;
import com.findwise.connect.api.event.ContentEvent;
import com.findwise.connect.api.event.JobExecution;
import com.findwise.connect.api.annotation.Parameter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONconnector implements TraverseConnector {
    @Parameter(name = "Path to folder with JSON files", required = true)
    private String path;
    //"C:\\Users\\Emelie\\AppData\\Local\\VirtualStore\\Program Files (x86)\\GnuWin32\\bin\\s2-corpus-000\\s2-corpus-000";
    //"C:\Users\Emelie\Dropbox\Chalmers\exjobb\Data";

    @Override
    public void run(JobExecution jobExecution) throws Exception {
        // Process all files in the specified directory
        final File folder = new File(path);
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            } else {
                processFile(fileEntry, jobExecution);
            }
        }
    }

    @Override
    public ConnectorMode getConnectorMode() {
        return ConnectorMode.STATEFUL;
    }

    /*
    * Add all JSONs in the file.
    * Assumes that the JSONs are specified on separate lines.
    * */
    private void processFile(final File file, JobExecution jobExecution){
        JSONParser parser = new JSONParser();
        String line;
        ArrayList<JSONObject> myJsonArr = new ArrayList<JSONObject>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            // each line is assumed to be one JSON object.
            while ((line = br.readLine()) != null) {
                try{
                    Object obj = parser.parse(line);
                    JSONObject jsonObject =  (JSONObject) obj;
                    jobExecution.publish(ContentEvent.newAddEvent(createJsonDocument(jsonObject)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Create a ContentDocument.ContentDocumentBuilder document with fields from the JSON argument.
    * */
    private ContentDocument.ContentDocumentBuilder createJsonDocument(JSONObject jsonObject){
        // make sure that there is an id field
        //TODO can two ContentDocuments have the same id?
        jsonObject.putIfAbsent("id", "unidentified");

        // create document to fill out below
        ContentDocument.ContentDocumentBuilder doc = ContentDocument.create(jsonObject.get("id").toString());

        // create the list of keys to process
        List<String> selectedKeys = new ArrayList<String>();

        // Add only a selected number of fields for processing the JSON
        selectedKeys.add("authors");
        selectedKeys.add("year");
        selectedKeys.add("title");
        selectedKeys.add("inCitations");
        selectedKeys.add("outCitations");

        /*
        // GENERAL VERSION, not needed for the moment
        // Add all the fields from the JSON
        for (Object key : jsonObject.keySet()) {
            // Make sure that it is a string
            if (key instanceof String) {
                selectedKeys.add((String) key);
            }
        }
        */

        // Add the fields corresponding to the selected keys
        for (String key : selectedKeys) {
            // Check for null
            if (jsonObject.get(key) == null) {
                setToEmpty(key, jsonObject);
            }

            // Cast to correct type and add
            if (jsonObject.get(key) instanceof String) {
                doc.addField(key, (String) jsonObject.get(key));
            } else if (jsonObject.get(key) instanceof Long) {
                doc.addField(key, jsonObject.get(key).toString());
            } else if (jsonObject.get(key) instanceof List) {
                List jArr = (List) jsonObject.get(key);
                // If key is author, the list is a list of JSON objects. Only the name is kept, turning
                // it into a list of strings.
                if (key.equals("authors")) {
                    for (int i = 0; i < jArr.size(); i++) {
                        jArr.set(i, makeSureObjectIsString(((JSONObject) jArr.get(i)).get("name")));
                    }
                } else {
                    for (int i = 0; i < jArr.size(); i++) {
                        jArr.set(i, makeSureObjectIsString(jArr.get(i)));
                    }
                }
                doc.addField(key, (Iterable<String>) jArr);
            }
        }
        return doc;
    }

    /*
     * Make sure that fields in the JSON object that are supposed to be empty are so (i.e. not null).
     * If the field is a JSON array, it is set to an empty array.
     * In all other cases the field is set to an empty string.
     * */
    private void setToEmpty(Object key, JSONObject jsonObject) {
        if (jsonObject.get(key) instanceof JSONArray) {
            jsonObject.put(key, new JSONArray());
        } else{
            jsonObject.put(key, "");
        }
    }

    /*
     * See to that the object is an instance of String.
     * * OBS fails to check for null, could crash in toString.
     * */
    private String makeSureObjectIsString(Object obj){
        if(obj instanceof String) {
            return (String) obj;
        }
        return obj.toString();
    }

} // End of class
