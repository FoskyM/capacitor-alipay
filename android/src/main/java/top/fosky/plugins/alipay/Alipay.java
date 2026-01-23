package top.fosky.plugins.alipay;

import com.getcapacitor.Logger;

public class Alipay {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
