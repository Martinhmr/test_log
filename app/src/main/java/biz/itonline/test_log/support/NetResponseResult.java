package biz.itonline.test_log.support;

import org.json.JSONObject;

public interface NetResponseResult {
    void processNetResponse(String task, JSONObject js);
    void processNetError(String task, String msg);
}
