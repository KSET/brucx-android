package kset.ivan.rasus.brucx;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by cona on 21.12.16..
 */

public class TicketType {


    @SerializedName("activeFrom")
    @Expose
    private String activeFrom;
    @SerializedName("activeTo")
    @Expose
    private String activeTo;
    @SerializedName("defaultPrice")
    @Expose
    private Integer defaultPrice;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("requiredInTable")
    @Expose
    private String requiredInTable;

    public String getActiveFrom() {
        return activeFrom;
    }

    public void setActiveFrom(String activeFrom) {
        this.activeFrom = activeFrom;
    }

    public String getActiveTo() {
        return activeTo;
    }

    public void setActiveTo(String activeTo) {
        this.activeTo = activeTo;
    }

    public Integer getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(Integer defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequiredInTable() {
        return requiredInTable;
    }

    public void setRequiredInTable(String requiredInTable) {
        this.requiredInTable = requiredInTable;
    }

}
