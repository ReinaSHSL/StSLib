package com.evacipated.cardcrawl.mod.stslib.cards.interfaces;

import com.evacipated.cardcrawl.mod.stslib.patches.BranchingUpgradesPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface BranchingUpgradesCard {

    void branchUpgrade();

    default void setIsBranchUpgrade() {
        if (this instanceof AbstractCard) {
            AbstractCard c = (AbstractCard) this;
            BranchingUpgradesPatch.BranchingUpgradeField.isBranchUpgraded.set(c, true);
            c.upgraded = true;
            branchUpgrade();
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
            }
            if (c.upgradedDamage) {
                c.isDamageModified = true;
            }
            if (c.upgradedMagicNumber) {
                c.isMagicNumberModified = true;
            }
        }
    }
}
