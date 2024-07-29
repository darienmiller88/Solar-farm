package learn.solar.models;

public enum Material {
    McSi("McSi"),
    MonoSi("MonoSi"),
    ASi("ASi"),
    CdTe("CdTe"),
    CIGS("CIGS");

    Material(String materialName){
        this.materialName = materialName;
    }

    @Override
    public String toString(){
        return materialName;
    }

    // public static is

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

        return null;
        // throw new IllegalArgumentException("No enum with the following name: " + materialName);
    }

    private String materialName;
}
