package layout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philip on 2/26/17.
 */

public class addPlayer {
    public static ArrayList<player_model> playerlist;

    public static  ArrayList<player_model> getPlayerlist(){
        if (playerlist == null) {
            playerlist = new ArrayList<>();
        }
        return playerlist;
    }
    public static void addPlayer(String name, String img, int alive, int playerNR, String uniqueKEy, Activity mActivety){
        if (playerlist == null) {
            playerlist = new ArrayList<>();
        }
        Boolean playerExists = false;
        for (int i = 0; i < playerlist.size(); i++){
            if (playerlist.get(i).getUniqueKEy().equals(uniqueKEy)){
                playerExists = true;
                playerlist.get(i).overWrite(new player_model(name, img, alive, playerNR, uniqueKEy, mActivety));
            }
        }
        if (! playerExists) {
            playerlist.add(new player_model(name, img, alive, playerNR, uniqueKEy, mActivety));
        }
    }
    static JSONArray getJsonArray(ArrayList<Integer> playerNRList){
        JSONArray jsonArray = new JSONArray();
        for (Integer nr:playerNRList) {
            jsonArray.put(playerlist.get(nr).getJSONObject());
        }
        return jsonArray;
    }
    static ArrayList<player_model> JsonArrayToArrayList(JSONArray jsonArray){
        ArrayList<player_model> array = new ArrayList<>();
        for (int i=0; i < jsonArray.length(); i++){
            try {
                array.add(new player_model("None", "None", 0, 1, "None", null));
                JSONObject obj = jsonArray.getJSONObject(i);
                array.get(i).jsonObjectToArrayListObject(obj);
            } catch (JSONException e) {
                //trace("DefaultListItem.toString JSONException: "+e.getMessage());
                Log.e("MYAPP", "unexpected JSON exception", e);
            }
        }
        return array;
    }
    static void addToExistingPersons(ArrayList<player_model> givenList){
        if (givenList.size()>0){
            for (int i=0; i<givenList.size();i++) {
                Boolean newPlayer = true;
                for (int m = 0; m < playerlist.size(); m++) {
                    if (playerlist.get(m).getUniqueKEy().equals(givenList.get(i).getUniqueKEy())) {
                        newPlayer = false;
                        //Overwrite
                        playerlist.get(m).overWrite(givenList.get(i));
                        break;
                    }
                }
                if (newPlayer) {
                    String name = givenList.get(i).getName();
                    String img = givenList.get(i).getImg();
                    int alive = givenList.get(i).isAlive();
                    String uniqueKEy = givenList.get(i).getUniqueKEy();
                    playerlist.add(new player_model(name, img, alive,1, uniqueKEy, null));
                    //now overwirte the new player with the given player
                    playerlist.get(playerlist.size()-1).overWrite(givenList.get(i));
                }
            }
        }
        //Check for delet
        for (int i=0;i<playerlist.size();i++){
            if (playerlist.get(i).getDeletMe()){
                //Delet if deletMe
                playerlist.remove(i);
            }
        }
    }
    static player_model host(){
        int nr=-1;
        for (int i=0; i<playerlist.size();i++) {
            if (playerlist.get(i).getHost()) {
                nr = i;
                break;
            }
        }
        if (nr == -1){
            return null;
        }
        else {
            return playerlist.get(nr);
        }
    }
    static player_model me(String uniqkey){
        int nr=0;
        for (int i=0; i<playerlist.size();i++) {
            if (playerlist.get(i).getUniqueKEy().equals(uniqkey)) {
                nr = i;
                break;
            }
        }
        return playerlist.get(nr);
    }
}
