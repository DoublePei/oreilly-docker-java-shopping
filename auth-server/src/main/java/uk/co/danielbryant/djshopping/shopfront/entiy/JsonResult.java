package uk.co.danielbryant.djshopping.shopfront.entiy;

import lombok.Data;

@Data
public class JsonResult {
    private int code;
    private String data;
    private String message;

   public JsonResult(int code, String data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
}
