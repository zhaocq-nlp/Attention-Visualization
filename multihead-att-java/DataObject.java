import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataObject {

    public int numSamples = 0;
    public List<String> attentionFieldList;


    private JSONObject dataObject = null;

    public DataObject(String filename){
        this.reload(filename);
    }

    public JSONObject get(int index){
        try {
            JSONObject obj = this.dataObject.getJSONObject(String.format("%d", index));
            return obj;
        } catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        return null;
    }

    public String getAttentionType(int index, String attentionField){
        JSONObject instanceObj = this.get(index);
        try{
            JSONArray attLists = instanceObj.getJSONArray("attentions");
            for (int i = 0; i < attLists.length(); ++i) {
                JSONObject obj = attLists.getJSONObject(i);
                if(obj.getString("name").equals(attentionField)){
                    return obj.getString("type");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        System.exit(0);
        return null;
    }


    public JSONArray getAttentionWeight(int index, String attentionField){
        JSONObject instanceObj = this.get(index);
        try{
            JSONArray attLists = instanceObj.getJSONArray("attentions");
            for (int i = 0; i < attLists.length(); ++i) {
                JSONObject obj = attLists.getJSONObject(i);
                if(obj.getString("name").equals(attentionField)){
                    return obj.getJSONArray("value");
                }
            }
        } catch(Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        System.exit(0);
        return null;
    }

    public void reload(String filename){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
            String str = br.readLine();
            br.close();
            this.dataObject = new JSONObject(str);

            this.numSamples = this.dataObject.length();
            this.attentionFieldList = new ArrayList<String>();
            JSONArray attArray = this.dataObject.getJSONObject("0").getJSONArray("attentions");
            for (int i = 0; i < attArray.length(); ++i) {
                JSONObject obj = attArray.getJSONObject(i);
                String type =obj.getString("type");
                if (type.equals("multihead") || type.equals("simple")) {
                    this.attentionFieldList.add(obj.getString("name"));
                } else{
                    System.err.println(String.format("Error with type: %s", type));
                    System.exit(0);
                }
            }
            Collections.sort(this.attentionFieldList);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
