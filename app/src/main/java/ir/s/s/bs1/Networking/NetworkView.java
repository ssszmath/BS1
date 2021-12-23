package ir.s.s.bs1.Networking;

import org.json.JSONObject;

public interface NetworkView {
    void successLoad(JSONObject jsonObject,String event);
    void errorLoad(String error_text,String event);
}
