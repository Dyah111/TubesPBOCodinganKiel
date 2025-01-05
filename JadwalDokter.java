import java.sql.*;
import java.util.*;

public class JadwalDokter {
    private String dokterId;
    private String jadwal;
    private String jadwal_mulai;
    private String jadwal_selesai;

    public JadwalDokter(String dokterId, String jadwal, String jadwal_mulai, String jadwal_selesai) {
        this.dokterId =jadwal_selesai;
        this.jadwal = jadwal;
        this.jadwal_mulai = jadwal_mulai;
        this.jadwal_selesai = jadwal_selesai;
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

    
    public String getJadwal_mulai() {
        return jadwal_mulai;
    }

    public void setJadwal_mulai(String jadwal_mulai) {
        this.jadwal_mulai = jadwal_mulai;
    }

    public String getJadwal_selesai() {
        return jadwal_selesai;
    }

    public void setJadwal_selesai(String jadwal_selesai) {
        this.jadwal_selesai = jadwal_selesai;
    }
}