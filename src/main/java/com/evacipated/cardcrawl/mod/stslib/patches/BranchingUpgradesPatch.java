package com.evacipated.cardcrawl.mod.stslib.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import javassist.CtBehavior;

import java.util.ArrayList;

public class BranchingUpgradesPatch {

    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CLASS
    )
    public static class BranchingUpgradeField {
        public static SpireField<Boolean> isBranchUpgraded = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = SpirePatch.CLASS
    )
    public static class BranchingUpgradePreviewCardField {
        public static SpireField<AbstractCard> branchUpgradePreviewCard = new SpireField<>(() -> null);
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "render"
    )
    public static class RenderBranchingUpgrade {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(GridCardSelectScreen __instance) {
            AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "hoveredCard");
            if (c instanceof BranchingUpgradesCard) {
                BranchingUpgradesCard previewCard = (BranchingUpgradesCard) BranchingUpgradePreviewCardField.branchUpgradePreviewCard.get(__instance).makeStatEquivalentCopy();
                previewCard.branchUpgrade();
                previewCard.displayBranchUpgrades();
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator
        {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
            {
                Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.cards.AbstractCard", "makeStatEquivalentCopy");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
