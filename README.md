
Very very crude attempt at source style airacceleration and bhop in Minecraft (Forge). You can change the acceleration settings in the code.

- **Air Strafing**: When a player is in the air, they can adjust their direction using the A and D keys. The player's horizontal movement speed in the air can be adjusted based on their strafing input. 
- **Air Acceleration**: Accelerate mid air with strafes
- **Automatic Bunny Hopping**: By holding the space bar, players will automatically jump upon landing, enabling continuous hopping, or "bunny hopping."
- **Speed Boosts**: Upon landing, if the player's air strafe acceleration is equal to or above the speed of the Potion of Swiftness effect prior to landing, they will receive a speed boost. The boost is amplified by the number of consecutive bunny hops performed, resetting if the player lands without holding the space bar or if their speed drops below the threshold.

Untested and I have no idea if this even works
