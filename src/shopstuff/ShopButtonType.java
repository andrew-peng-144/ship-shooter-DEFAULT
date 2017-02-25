package shopstuff;

public enum ShopButtonType {
	// screw the convention i dont feel like typing in all caps & underscores
	upgradeCannons, // changes sprite, upgrades damage, upgrades firerate and
					// velocity, eventually adds additional shots
	upgradeMaxHp,
	upgradeEnergy, //increases max energy AND regen rate
	upgradeDefense,
	// abilities
	
	upgradeMissiles, // will increase the splash radius, and at certain levels
						// will ADD missiles, and eventually homing
	upgradeShield, // will increase shield duration
}
