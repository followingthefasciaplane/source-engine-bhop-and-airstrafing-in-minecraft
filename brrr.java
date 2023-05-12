import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

@Mod.EventBusSubscriber
public class CustomPlayerMovement {
    private static final double AIR_ACCELERATION = 2.0f;
    private static final double SIDE_STRAFE_ACCELERATION = 50f;
    private static final double STRAFE_SYNC_BONUS = 1.5f;
    private static final double BUNNY_HOP_MULTIPLIER = 1.2f;
    private static final double SPEED_BOOST_THRESHOLD = 1.0f;

    private static float lastStrafe = 0;
    private static int strafeStreak = 0;
    private static int bunnyHopStreak = 0;
    private static boolean wasOnGround = false;

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        Vector3d motion = player.getMotion();

        if (!player.onGround) {
            // Air movement, strafing
            if (player.moveStrafing != 0) {
                // Check if player is strafing in the same direction as the last tick
                if (player.moveStrafing == lastStrafe) {
                    strafeStreak++;
                } else {
                    strafeStreak = 0;
                }

                lastStrafe = player.moveStrafing;

                double strafeBonus = strafeStreak > 0 ? STRAFE_SYNC_BONUS : 1;
                double strafeSpeed = SIDE_STRAFE_ACCELERATION * strafeBonus * event.player.world.getGameTime();

                // Calculate the strafe vector based on the player's camera angle
                Vector3d lookVec = player.getLookVec();
                Vector3d strafeVec = new Vector3d(-lookVec.z, 0, lookVec.x).normalize().scale(strafeSpeed);

                motion = motion.add(strafeVec);
                player.setMotion(motion);
            }

            // Air acceleration
            double accelSpeed = AIR_ACCELERATION * event.player.world.getGameTime();
            motion = new Vector3d(motion.x * accelSpeed, motion.y, motion.z * accelSpeed);
            player.setMotion(motion);

            // Bunny hopping
            if (wasOnGround && player.isJumping) {
                motion = motion.scale(BUNNY_HOP_MULTIPLIER);
                player.setMotion(motion);
                bunnyHopStreak++;
            } else if (!player.isJumping) {
                bunnyHopStreak = 0;
            }
        } else {
            // When player touches the ground
            if (player.isJumping && motion.length() >= SPEED_BOOST_THRESHOLD) {
                // Make the player jump
                player.jump();

                // Apply speed boost
                player.addPotionEffect(new EffectInstance(Effects.SPEED, 2, bunnyHopStreak, false, false));
            } else {
                bunnyHopStreak = 0;
            }
        }

        wasOnGround = player.onGround;
    }
}
