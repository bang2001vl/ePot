package exam.nlb2t.epot.ClassData;

import java.util.Date;

public class UserInfo {

    public UserInfo() {}

    private String iDAccount;

    public String getiDAccount() {
        return iDAccount;
    }

    public void setiDAccount(String iDAccount){
        this.iDAccount = iDAccount;
    }

    private String name;

    public String getName(){
        return iDAccount;
    }

    public void setName(String name){
        this.name = name;
    }

    private Date birthday;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    private String shopName;

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    private Date joinDay;

    public Date getJoinDay() {
        return joinDay;
    }

    public void setJoinDay(Date joinDay) {
        this.joinDay = joinDay;
    }

    private Long follower;

    public Long getFollower() {
        return follower;
    }

    public void setFollower(Long follower) {
        this.follower = follower;
    }

    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserInfo(String iDAccount, String name, Date birthday, String address, String email, String phone, String username, String password, String gender, String shopName, Date joinDay, Long follower, String description) {
        this.name = name;
        this.iDAccount = iDAccount;
        this.address = address;
        this.birthday = birthday;
        this.description = description;
        this.email = email;
        this.follower =follower;
        this.gender = gender;
        this.joinDay = joinDay;
        this.password = password;
        this.username = username;
        this.shopName = shopName;
        this.phone = phone;
    }
}
