package ir.s.s.bs1.Networking;

import java.util.Map;

public interface NetworkPresenter {
    void loadDataFromServer(Map<String,String> body, String endUrl, String event);
}
