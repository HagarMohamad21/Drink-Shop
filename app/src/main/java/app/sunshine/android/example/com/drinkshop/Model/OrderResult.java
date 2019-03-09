package app.sunshine.android.example.com.drinkshop.Model;

public class OrderResult {
    String OrderDetail,OrderAddress,UserPhone,OrderDate,OrderComment;
    int OrderId,OrderStatus;
    float price;

    public OrderResult(String orderDetail, String orderAddress, String userPhone, String orderDate, String orderComment, int orderId, int orderStatus, float price) {
        OrderDetail = orderDetail;
        OrderAddress = orderAddress;
        UserPhone = userPhone;
        OrderDate = orderDate;
        OrderComment = orderComment;
        OrderId = orderId;
        OrderStatus = orderStatus;
        this.price = price;
    }

    public String getOrderDetail() {
        return OrderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        OrderDetail = orderDetail;
    }

    public String getOrderAddress() {
        return OrderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        OrderAddress = orderAddress;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderComment() {
        return OrderComment;
    }

    public void setOrderComment(String orderComment) {
        OrderComment = orderComment;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public OrderResult() {
    }
}
