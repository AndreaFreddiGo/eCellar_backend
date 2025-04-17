package andrea_freddi.CAPSTONE_PROJECT.entities;

public enum BottleSize {
    SPLIT_187ML("Split (187 ml)", 0.187f),
    HALF_375ML("Half Bottle (375 ml)", 0.375f),
    STANDARD_750ML("Standard (750 ml)", 0.75f),
    MAGNUM_1_5L("Magnum (1.5 L)", 1.5f),
    JEROBOAM_3L("Jeroboam (3 L)", 3.0f),
    REHOBOAM_4_5L("Rehoboam (4.5 L)", 4.5f),
    METHUSELAH_6L("Methuselah (6 L)", 6.0f),
    SALMANAZAR_9L("Salmanazar (9 L)", 9.0f),
    BALTHAZAR_12L("Balthazar (12 L)", 12.0f),
    NEBUCHADNEZZAR_15L("Nebuchadnezzar (15 L)", 15.0f),
    SOLOMON_18L("Solomon (18 L)", 18.0f),
    MELCHIOR_18L("Melchior (18 L)", 18.0f),
    SOVEREIGN_26_25L("Sovereign (26.25 L)", 26.25f),
    GOLIATH_27L("Goliath (27 L)", 27.0f),
    PRIMAT_27L("Primat (27 L)", 27.0f),
    MELCHIZEDEK_30L("Melchizedek (30 L)", 30.0f),
    OTHER("Other", null);

    private final String label;
    private final Float volumeInLiters;

    BottleSize(String label, Float volumeInLiters) {
        this.label = label;
        this.volumeInLiters = volumeInLiters;
    }

    public String getLabel() {
        return label;
    }

    public Float getVolumeInLiters() {
        return volumeInLiters;
    }
}

