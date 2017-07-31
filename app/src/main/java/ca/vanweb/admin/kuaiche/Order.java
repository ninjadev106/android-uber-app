package ca.vanweb.admin.kuaiche;


public class Order {

    private String mID;
    private String mFromAddress;
    private String mToAddress;
    private String mCreateDateTime;
    private String mTotalMoney;
    private String mOrderStatusText;
    private String mOrderNumber;
    private String mPassengerMobile;
    private String mDuration;
    private String mDistance;
    private String mAcceptDateTime;
    private String mPickupDateTime;
    private String mFinishDateTime;

    public String getOrderNumber() {
        return mOrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        mOrderNumber = orderNumber;
    }

    public String getPassengerMobile() {
        return mPassengerMobile;
    }

    public void setPassengerMobile(String passengerMobile) {
        mPassengerMobile = passengerMobile;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public String getAcceptDateTime() {
        return mAcceptDateTime;
    }

    public void setAcceptDateTime(String acceptDateTime) {
        mAcceptDateTime = acceptDateTime;
    }

    public String getPickupDateTime() {
        return mPickupDateTime;
    }

    public void setPickupDateTime(String pickupDateTime) {
        mPickupDateTime = pickupDateTime;
    }

    public String getFinishDateTime() {
        return mFinishDateTime;
    }

    public void setFinishDateTime(String finishDateTime) {
        mFinishDateTime = finishDateTime;
    }

    public Order() {
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getFromAddress() {
        return mFromAddress;
    }

    public void setFromAddress(String fromAddress) {
        mFromAddress = fromAddress;
    }

    public String getToAddress() {
        return mToAddress;
    }

    public void setToAddress(String toAddress) {
        mToAddress = toAddress;
    }

    public String getCreateDateTime() {
        return mCreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        mCreateDateTime = createDateTime;
    }

    public String getTotalMoney() {
        return mTotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        mTotalMoney = totalMoney;
    }

    public String getOrderStatusText() {
        return mOrderStatusText;
    }

    public void setOrderStatusText(String orderStatusText) {
        mOrderStatusText = orderStatusText;
    }
}
