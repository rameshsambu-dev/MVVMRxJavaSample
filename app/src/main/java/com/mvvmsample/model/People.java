
package com.mvvmsample.model;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class People implements Serializable {

  @SerializedName("gender") public String gender;

  @SerializedName("name") public Name name;

  @SerializedName("location") public Location location;

  @SerializedName("email") public String mail;

  @SerializedName("login") public Login login;

  @SerializedName("phone") public String phone;

  @SerializedName("cell") public String cell;

  @SerializedName("picture") public Picture picture;

  public String fullName;

  public boolean hasEmail() {
    return mail != null && !mail.isEmpty();
  }
}
