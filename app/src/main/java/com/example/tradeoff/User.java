package com.example.tradeoff;

 public class User {

     private String firstName;
     private String lastName;
     private String email;
     private String password;
     private String phone;
     private String activity;
     private String region;


     public User() {
         this.firstName = "";
         this.lastName = "";
         this.email = "";
         this.password = "";
         this.phone = "";
         this.activity = "";
         this.region = "";
     }

     public User(String _Fname, String Lname, String email, String password, String phone, String activ, String adress) {
         this.firstName = _Fname;
         this.lastName = Lname;
         this.email = email;
         this.password = password;
         this.phone = phone;
         this.activity = activ;
         this.region = adress;
     }

     public String getEmail() {
         return email;
     }

     public String getFirstName() {
         return firstName;
     }

     public String getLastName() {
         return lastName;
     }

     public String getActivity() {
         return activity;
     }

     public String getPassword() {
         return password;
     }

     public String getPhone() {
         return phone;
     }

     public void setActivity(String activity) {
         this.activity = activity;
     }

     public String getRegion() {
         return region;
     }

     public void setRegion(String region) {
         this.region = region;
     }

     public void setEmail(String email) {
         this.email = email;
     }

     public void setFirstName(String firstName) {
         this.firstName = firstName;
     }

     public void setLastName(String lastName) {
         this.lastName = lastName;
     }

     public void setPassword(String password) {
         this.password = password;
     }

     public void setPhone(String phone) {
         this.phone = phone;
     }
 }
