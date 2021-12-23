package ir.s.s.bs1.Networking;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.Map;


public class NetworkModel implements NetworkPresenter {

    NetworkView networkView;
    public NetworkModel(NetworkView networkView) {
        this.networkView=networkView;
    }

    @Override
    public void loadDataFromServer(Map<String,String> body, String endUrl, final String event) {
        AndroidNetworking.post("https://tsma-co.ir/task/")
                .addBodyParameter(body)
                .setTag(endUrl)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        networkView.successLoad(response,event);
                    }

                    @Override
                    public void onError(ANError anError) {
                        networkView.errorLoad(anError.getErrorBody(),event);
                    }
                });
    }
}
