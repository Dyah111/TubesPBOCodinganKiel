import java.util.List;

public class Dokter {
    private String id;
    private String name;
    private String spesialis;
    private int kapasitas;
    private int antrian;

    public Dokter(String id, String name, String spesialis, int kapasitas, int antrian) {
        this.id = id;
        this.name = name;
        this.spesialis = spesialis;
        this.kapasitas = kapasitas;
        this.antrian = antrian;
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

    public int getKapasitas() {
        return kapasitas;
    }
    
    public int getAntrian() {
        return antrian;
    }
    
    public void setKapasitas(int kapasitas) {
        this.kapasitas = kapasitas;
    }
    
    public void setAntrian(int antrian) {
        this.antrian = antrian;
    }
}