package main.java.learn.solar.models;

public enum Material {
    McSi("Multicrystalline Silicon"),
    MonoSi("Monocrystalline Silicon"),
    ASi("Amorphous Silicon"),
    CdTe("Cadmium Telluride"),
    CIGS("Copper Indium Gallium Selenide");

    Material(String materialName){
        this.materialName = materialName;
    }

    @Override
    public String toString(){
        return materialName;
    }

    private String materialName;
}
