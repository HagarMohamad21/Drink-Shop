package app.sunshine.android.example.com.drinkshop.Model;

public class Token {
    String Token,IsServerToken, Phone;

    public Token(String Token, String IsServerToken, String Phone) {
        this.Token = Token;
        this.IsServerToken = IsServerToken;
        this.Phone = Phone;
    }
    public Token(){}

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getIsServerToken() {
        return IsServerToken;
    }

    public void setIsServerToken(String IsServerToken) {
        this.IsServerToken = IsServerToken;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }
}
