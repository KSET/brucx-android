package kset.ivan.rasus.brucx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cona on 19.12.16..
 */

public class Studenti {


    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("freshmen")
    @Expose
    private Boolean freshmen;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("jmbag")
    @Expose
    private String jmbag;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nfcId")
    @Expose
    private String nfcId;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("tickets")
    @Expose
    private List<Ticket> tickets = null;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getFreshmen() {
        return freshmen;
    }

    public void setFreshmen(Boolean freshmen) {
        this.freshmen = freshmen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getJmbag() {
        return jmbag;
    }

    public void setJmbag(String jmbag) {
        this.jmbag = jmbag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNfcId() {
        return nfcId;
    }

    public void setNfcId(String nfcId) {
        this.nfcId = nfcId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Studenti{" +
                "email='" + email + '\'' +
                ", freshmen=" + freshmen +
                ", image='" + image + '\'' +
                ", jmbag='" + jmbag + '\'' +
                ", name='" + name + '\'' +
                ", nfcId='" + nfcId + '\'' +
                ", surname='" + surname + '\'' +
                ", tickets=" + tickets +
                '}';
    }
}



