package pt.feup.worldlivelink.Photo;

import java.io.Serializable;

public class PhotoBean implements Serializable {

    String filename;

    byte[] bytes;

    String alumni_id;


    public PhotoBean() {}



    public String getFilename() {
        return filename;
    }

    public PhotoBean setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public PhotoBean setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    public String getAlumni_id() {
        return alumni_id;
    }

    public PhotoBean setAlumni_id(String alumni_id) {
        this.alumni_id = alumni_id;
        return this;
    }
}
