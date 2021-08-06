package net.guizhanss.minecraft.dynatech.utils;

import javax.annotation.Nonnull;

public class FluidUtils {
    private FluidUtils() {
    }

    @Nonnull
    public static String getFluidType(@Nonnull String fluidName){
        switch(fluidName){
            case "WATER":
                return "水";
            case "LAVA":
                return "岩浆";
            case "NO_FLUID":
                return "没有液体";
            default:
                return fluidName;
        }
    }
}
