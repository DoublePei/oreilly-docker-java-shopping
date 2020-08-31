package uk.co.danielbryant.djshopping.shopfront.entiy;


public enum ResultCode {

    SUCCESS(200), FAIL(500),NOT_LOGIN(400), SYS_ERROR(505), TOO_FREQUENT(426), UNKNOWN_ERROR(600);
    private int code;

    ResultCode(int code) {
        this.code = code;
    }

    public static ResultCode of(String code) {
        return of(Integer.parseInt(code));
    }

    public static ResultCode of(int code) {
        for (ResultCode resultCode : ResultCode.values()) {
            if (resultCode.code == code) {
                return resultCode;
            }
        }
        throw new IllegalArgumentException("unsupported resultCode " + code);
    }

    public int getCode() {
        return this.code;
    }
}
