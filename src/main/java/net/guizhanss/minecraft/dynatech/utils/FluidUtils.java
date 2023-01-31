package net.guizhanss.minecraft.dynatech.utils;

import javax.annotation.Nonnull;

public class FluidUtils {
    private FluidUtils() {
    }

    @Nonnull
    public static String getFluidType(@Nonnull String fluidName){
        return switch (fluidName) {
            case "WATER" -> "水";
            case "LAVA" -> "岩浆";
            case "NO_FLUID" -> "没有液体";
            default -> fluidName;
        };
    }
}
