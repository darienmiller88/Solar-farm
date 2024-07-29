package learn.solar.models;

public class Panel {
    private int id;
    private String section;
    private int row;
    private int column;
    private int installationYear;
    private Material material;
    private boolean tracking; 

    public Panel(){

    }

    public Panel(int id, String section, int row, int column, int installationYear, Material material, boolean tracking){
        this.id = id;
        this.section = section;
        this.row = row;
        this.column = column;
        this.installationYear = installationYear;
        this.material = material;
        this.tracking = tracking;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setSection(String section){
        this.section = section;
    }

    public String getSection(){
        return section;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getInstallationYear() {
        return installationYear;
    }

    public void setInstallationYear(int installationYear) {
        this.installationYear = installationYear;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    @Override
    public String toString(){
        return String.format("%s %s %s %s %s %s %s\n",
                id,
                section,
                row,
                column,
                installationYear,
                material.toString(),
                isTracking() ?  "not tracking" : "is tracking"
        );
    }
}
