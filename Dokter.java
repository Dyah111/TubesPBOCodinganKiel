import java.util.List;

public class Dokter {
    private String id;
    private String name;
    private String spesialis;

    public Dokter(String id, String name, String spesialis) {
        this.id = id;
        this.name = name;
        this.spesialis = spesialis;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpesialis() {
        return spesialis;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSpesialis(String spesialis) {
        this.spesialis = spesialis;
    }

    public List<JadwalDokter> getJadwal() {
        return JadwalDokterDAO.getJadwalByDokterId(this.id);
    }
}