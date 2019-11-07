import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JSONconnector{

    private static String path = "//home//fallman//testJava";

    public static void run() {
        // Process all files in the specified directory
        final File folder = new File(path);

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                continue;
            } else {
                processFile(fileEntry);
            }
        }
    }
    /*
     * Add all JSONs in the file.
     * Assumes that the JSONs are specified on separate lines.
     * */
    private static void processFile(final File file){
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            // each line is assumed to be one JSON object.
            while ((line = br.readLine()) != null) {
                try {
                    // read the string as a JSON
                    JsonReader jsonRdr = Json.createReader(new StringReader(line));
                    // assume that it was a json object (and not a json array)
                    JsonObject doc = jsonRdr.readObject();
                    jsonRdr.close();
                    addJsonToIndex(doc);
                } catch (Exception e) {
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
    private static void addJsonToIndex(JsonObject jsonObj){
        // select which fields to keep
        List<String> selectedKeys = selectFieldsToKeep();

        // check if any of the fields are null
        selectedKeys = eliminateKeyIfValueIsNull(selectedKeys, jsonObj);

        // Add the fields corresponding to the selected keys
        for (String key : selectedKeys) {
            // Cast to correct type and add
            if (jsonObj.get(key) instanceof JsonString) {
                String value = jsonObj.getString(key);
                //doc.addField(key, value);
                //System.out.println("Key: " + key + " value: "+ value);
            } else if (jsonObj.get(key) instanceof JsonNumber) {
                String value = jsonObj.get(key).toString();
                //doc.addField(key, value);
                //System.out.println("Key: " + key + " value: "+ value);
            } else if (jsonObj.get(key) instanceof JsonArray) {
                List<String> value = new ArrayList<>();
                // make sure that all objects in the list are Strings
                JsonArray jArr = ((JsonArray) jsonObj.get(key));
                for (int i = 0; i < jArr.size(); i++) {
                    value.add(jArr.get(i).toString());
                }
                //doc.addField(key, value);
                //System.out.println("Key: " + key + " value: "+ value + " real value: " + jsonObj.get(key).toString());
            }
        }
    }

    // create 10 shards in my node, one index
    // add abstract and year and authors?
    // does it matter

    private static List<String> selectFieldsToKeep(){
        // create the list of keys to process
        List<String> selectedKeys = new ArrayList<String>();

        // Add only a selected number of fields for processing the JSON
        selectedKeys.add("title");
        selectedKeys.add("inCitations");
        selectedKeys.add("outCitations");

        /*// GENERAL VERSION, not needed for the moment
        // Add all the fields from the JSON
        for (Object key : jsonObject.keySet()) {
            // Make sure that it is a string
            if (key instanceof String) {
                selectedKeys.add((String) key);
            }
        }*/
        return selectedKeys;
    }

    // keep only keys with non-null value
    private static List<String> eliminateKeyIfValueIsNull(List<String> selectedKeys, JsonObject jsonObject){
        // create the list of keys to process
        List<String> keysWithNotNullValue = new ArrayList<String>();

        for (String key : selectedKeys) {
            if(jsonObject.get(key) != null){
                keysWithNotNullValue.add(key);
            }
        }
        return keysWithNotNullValue;
    }
} // End of class