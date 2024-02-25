package com.kneelawk.extramodintegrations.tconstruct;

import com.kneelawk.extramodintegrations.AbstractTiCIntegration;
import dev.emi.emi.api.EmiRegistry;

public class TiCIntegration extends AbstractTiCIntegration {
    @Override
    protected void registerImpl(EmiRegistry registry) {
        // register categories
        registry.addCategory(TiCCategories.CASTING_BASIN);
        registry.addCategory(TiCCategories.CASTING_TABLE);
        registry.addCategory(TiCCategories.MOLDING);
        registry.addCategory(TiCCategories.MELTING);
        registry.addCategory(TiCCategories.ALLOY);
        registry.addCategory(TiCCategories.ENTITY_MELTING);
        registry.addCategory(TiCCategories.FOUNDRY);
        registry.addCategory(TiCCategories.MODIFIER);
        registry.addCategory(TiCCategories.SEVERING);
        registry.addCategory(TiCCategories.PART_BUILDER);
        registry.addCategory(TiCCategories.MODIFIER_WORKTABLE);
    }
}
