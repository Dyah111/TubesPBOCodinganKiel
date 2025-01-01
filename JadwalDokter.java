import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JadwalDokter {
    private String dokterId;
    private String jadwal;

    public JadwalDokter(String dokterId, String jadwal) {
        this.dokterId = dokterId;
        this.jadwal = jadwal;
    }

    public String getDokterId() {
        return dokterId;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }
}