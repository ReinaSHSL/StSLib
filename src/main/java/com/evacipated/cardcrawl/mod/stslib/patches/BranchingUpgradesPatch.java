package com.evacipated.cardcrawl.mod.stslib.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.BranchingUpgradesCard;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
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
            method = SpirePatch.CLASS
    )
    public static class WaitingForBranchUpgradeSelection {
        public static SpireField<Boolean> waitingForBranchUpgradeSelection = new SpireField<>(() -> false);
    }


    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = SpirePatch.CLASS
    )
    public static class IsBranchUpgrading {
        public static SpireField<Boolean> isBranchUpgrading = new SpireField<>(() -> false);
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "update"
    )
    public static class GetBranchingUpgrade {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(GridCardSelectScreen __instance) {
            AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "hoveredCard");
            if (c instanceof BranchingUpgradesCard) {
                AbstractCard previewCard = c.makeStatEquivalentCopy();
                BranchingUpgradesCard setBranchUpgradesCard = (BranchingUpgradesCard) previewCard;
                setBranchUpgradesCard.setIsBranchUpgrade();
                setBranchUpgradesCard.displayBranchUpgrades();
                BranchingUpgradePreviewCardField.branchUpgradePreviewCard.set(__instance, previewCard);
                WaitingForBranchUpgradeSelection.waitingForBranchUpgradeSelection.set(__instance, true);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.cards.AbstractCard", "makeStatEquivalentCopy");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "update"
    )
    public static class StupidFuckingUpdateBullshitImSoMadDontChangeThisClassNameKio {
        @SpireInsertPatch(
                rloc = 163
        )
        public static void Insert(GridCardSelectScreen __instance) {
            if (BranchingUpgradePreviewCardField.branchUpgradePreviewCard.get(__instance) != null) {
                BranchingUpgradePreviewCardField.branchUpgradePreviewCard.get(__instance).update();
            }
        }

    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "render"
    )
    public static class RenderBranchingUpgrade {
        @SpireInsertPatch(
                locator = Locator.class
        )
        public static SpireReturn Insert(GridCardSelectScreen __instance, SpriteBatch sb) {
            AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "hoveredCard");
            if (c instanceof BranchingUpgradesCard) {
                AbstractCard branchUpgradedCard = BranchingUpgradePreviewCardField.branchUpgradePreviewCard.get(__instance);
                c.current_x = (Settings.WIDTH * 0.36F);
                c.current_y = (Settings.HEIGHT / 2.0F);
                c.target_x = (Settings.WIDTH * 0.36F);
                c.target_y = (Settings.HEIGHT / 2.0F);
                c.render(sb);
                c.updateHoverLogic();
                __instance.upgradePreviewCard.drawScale = 0.9F;
                __instance.upgradePreviewCard.current_x = (Settings.WIDTH * 0.63F);
                __instance.upgradePreviewCard.current_y = (Settings.HEIGHT * 0.75F - (50 * Settings.scale));
                __instance.upgradePreviewCard.target_x = (Settings.WIDTH * 0.63F);
                __instance.upgradePreviewCard.target_y = (Settings.HEIGHT * 0.75F - (50 * Settings.scale));
                __instance.upgradePreviewCard.render(sb);
                __instance.upgradePreviewCard.updateHoverLogic();
                __instance.upgradePreviewCard.renderCardTip(sb);
                branchUpgradedCard.drawScale = 0.9F;
                branchUpgradedCard.current_x = (Settings.WIDTH * 0.63F);
                branchUpgradedCard.current_y = (Settings.HEIGHT / 4.0F + (50 * Settings.scale));
                branchUpgradedCard.target_x = (Settings.WIDTH * 0.63F);
                branchUpgradedCard.target_y = (Settings.HEIGHT / 4.0F + (50 * Settings.scale));
                branchUpgradedCard.render(sb);
                branchUpgradedCard.updateHoverLogic();
                branchUpgradedCard.renderCardTip(sb);
                if ((__instance.forUpgrade) || (__instance.forTransform) || (__instance.forPurge) || (__instance.isJustForConfirming) || (__instance.anyNumber)) {
                    __instance.confirmButton.render(sb);
                }
                CardGroup targetGroup = (CardGroup) ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "targetGroup");
                String tipMsg = (String) ReflectionHacks.getPrivate(__instance, GridCardSelectScreen.class, "tipMsg");
                if ((!__instance.isJustForConfirming) || (targetGroup.size() > 5)) {
                    FontHelper.renderDeckViewTip(sb, tipMsg, 96.0F * Settings.scale, Settings.CREAM_COLOR);
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher("com.megacrit.cardcrawl.screens.select.GridCardSelectScreen", "renderArrows");
                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }


    @SpirePatch(
            clz = GridSelectConfirmButton.class,
            method = "render"
    )
    public static class BranchUpgradeConfirm {
        public static SpireReturn Prefix(GridSelectConfirmButton __instance, SpriteBatch sb) {
            AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(AbstractDungeon.gridSelectScreen, GridCardSelectScreen.class, "hoveredCard");
            if (WaitingForBranchUpgradeSelection.waitingForBranchUpgradeSelection.get(AbstractDungeon.gridSelectScreen) && c instanceof BranchingUpgradesCard  ) {
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = GridCardSelectScreen.class,
            method = "cancelUpgrade"
    )
    public static class CancelUpgrade {
        public static void Prefix(GridCardSelectScreen __instance) {
            WaitingForBranchUpgradeSelection.waitingForBranchUpgradeSelection.set(__instance, false);
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "update"
    )
    public static class SelectBranchedUpgrade {
        public static void Postfix(AbstractCard __instance) {
            if (__instance.hb.hovered && InputHelper.justClickedLeft) {
                System.out.println("dab");
                if (BranchingUpgradeField.isBranchUpgraded.get(__instance)) {
                    IsBranchUpgrading.isBranchUpgrading.set(AbstractDungeon.gridSelectScreen, true);
                }
                WaitingForBranchUpgradeSelection.waitingForBranchUpgradeSelection.set(AbstractDungeon.gridSelectScreen, false);
            }
        }
    }

    @SpirePatch(
            clz = GridSelectConfirmButton.class,
            method = "update"
    )
    public static class DoBranchUpgrade {
        public static void Prefix(GridSelectConfirmButton __instance) {
            if (IsBranchUpgrading.isBranchUpgrading.get(AbstractDungeon.gridSelectScreen) && __instance.hb.hovered && InputHelper.justClickedLeft) {
                AbstractCard c = (AbstractCard) ReflectionHacks.getPrivate(AbstractDungeon.gridSelectScreen, GridCardSelectScreen.class, "hoveredCard");
                BranchingUpgradesCard upgradeCard = (BranchingUpgradesCard) c;
                System.out.println("BIG DAB");
                upgradeCard.setIsBranchUpgrade();
            }
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = "makeStatEquivalentCopy"
    )
    public static class CopiesRetainBranchUpgrade {
        public static void Postfix(AbstractCard __result, AbstractCard __instance) {
            if (__result.timesUpgraded < 0 && __result instanceof BranchingUpgradesCard) {
                for (int i = 0; i > __result.timesUpgraded; i--) {
                    BranchingUpgradesCard c = (BranchingUpgradesCard) __result;
                    c.setIsBranchUpgrade();
                }
            }
        }
    }

    @SpirePatch(
            clz = CardLibrary.class,
            method = "getCopy"
    )
    public static class SaveBranchingUpgrades {
        public static void Postfix(AbstractCard __result, CardLibrary __instance, String useless0, int upgradeCount, int useless1) {
            if (upgradeCount < 0 && __result instanceof BranchingUpgradesCard) {
                for (int i = 0; i > upgradeCount; i--) {
                    BranchingUpgradesCard c = (BranchingUpgradesCard) __result;
                    c.setIsBranchUpgrade();
                }
            }
        }
    }
}
