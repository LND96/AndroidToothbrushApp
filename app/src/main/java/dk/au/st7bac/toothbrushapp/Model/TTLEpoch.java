package dk.au.st7bac.toothbrushapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TTLEpoch {

    @SerializedName("N")
    @Expose
    private String n;

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

}
