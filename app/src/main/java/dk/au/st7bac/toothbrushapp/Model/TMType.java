package dk.au.st7bac.toothbrushapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TMType {

        @SerializedName("S")
        @Expose
        private String s;

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

    }
