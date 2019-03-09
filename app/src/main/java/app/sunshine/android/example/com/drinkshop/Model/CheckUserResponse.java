package app.sunshine.android.example.com.drinkshop.Model;

public class CheckUserResponse {
    private boolean exists;
    private String error_message;
    public CheckUserResponse(){}

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}
