package com.evacipated.cardcrawl.mod.stslib.cards.interfaces;

import com.evacipated.cardcrawl.mod.stslib.patches.BranchingUpgradesPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface BranchingUpgradesCard {

    void branchUpgrade();

    default void setIsBranchUpgrade() {
        if (this instanceof AbstractCard) {
            AbstractCard c = (AbstractCard) this;
            BranchingUpgradesPatch.BranchingUpgradeField.isBranchUpgraded.set(c, true);
            branchUpgrade();
            c.upgraded = true;
            if (c.timesUpgraded > 0) {
                c.timesUpgraded = -1;
            }
            c.timesUpgraded--;
        }
    }

    default void displayBranchUpgrades() {
        if (this instanceof AbstractCard) {
            AbstractCard c = (AbstractCard) this;
            if (c.upgradedCost) {
                c.isCostModified = true;
            }
            if (c.upgradedBlock) {
                c.isBlockModified = true;
                c.baseBlock = c.block;
            }
            if (c.upgradedDamage) {
                c.isDamageModified = true;
                c.baseDamage = c.damage;
            }
            if (c.upgradedMagicNumber) {
                c.isMagicNumberModified = true;
                c.baseMagicNumber = c.magicNumber;
            }
        }
    }
}
