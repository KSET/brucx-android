package kset.ivan.rasus.brucx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cona on 19.12.16..
 */

public class Ticket {

    @SerializedName("boughtOn")
    @Expose
    private String boughtOn;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("studentJmbag")
    @Expose
    private String studentJmbag;
    @SerializedName("ticketType")
    @Expose
    private TicketType ticketType;
    @SerializedName("ticketTypeId")
    @Expose
    private Integer ticketTypeId;
    @SerializedName("used")
    @Expose
    private Integer used;

    public Integer getUsed() {
        return used;
    }

    public String getBoughtOn() {
        return boughtOn;
    }

    public void setBoughtOn(String boughtOn) {
        this.boughtOn = boughtOn;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getStudentJmbag() {
        return studentJmbag;
    }

    public void setStudentJmbag(String studentJmbag) {
        this.studentJmbag = studentJmbag;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Integer getTicketTypeId() {
        return ticketTypeId;

    }

    public void setTicketTypeId(Integer ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }



}
