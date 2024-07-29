package learn.solar.models;

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

    /**
     * 
     * @param materialName
     * @return Material enum that matches materialName
     */
    public static Material fromMaterialName(String materialName) {
        for (Material material : Material.values()) {
            if (material.materialName.equalsIgnoreCase(materialName)) {
                return material;
            }
        }

        throw new IllegalArgumentException("No enum with the following name: " + materialName);
    }

    private String materialName;
}
